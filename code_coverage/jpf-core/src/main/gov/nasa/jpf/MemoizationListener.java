package gov.nasa.jpf;

import gov.nasa.jpf.jvm.bytecode.FRETURN;
import gov.nasa.jpf.jvm.bytecode.IRETURN;
import gov.nasa.jpf.vm.*;

import java.util.*;

public class MemoizationListener extends ListenerAdapter {
    /**
     * HashMap
     * class.method -> method cache
     */
    HashMap<String, MethodCache> methodCacheMap = new HashMap<>();
    int[] currentSlot;
    StackFrame currentStackFrame;

    /**
     * HashMap
     * refId -> object
     */
    class MethodCache<T> {
        ClassInfo ci;
        HashMap<Integer, ElementInfo> fieldsMap = new HashMap<>();
        HashMap<Integer, T> inputs = new HashMap<>();

        public boolean isCached(int[] input) {
            return inputs.containsKey(Arrays.hashCode(input));
        }

        public boolean setCache(int[] input, T result, VM vm, StackFrame stackFrame) {
            if (inputs.containsKey(Arrays.hashCode(input))) {
                return false;
            }
            int slotsLength = stackFrame.getSlots().length;
            for (int i = 0; i < slotsLength; i++) {
                if (stackFrame.isReferenceSlot(i)) {
                    if (!saveElements(input[i], vm)) {
                        return false;
                    }
                }
            }

            inputs.put(Arrays.hashCode(input), result);
            return true;
        }

        private boolean saveElements(Integer refId, VM vm) {
            ElementInfo inputElementInfo = vm.getHeap().get(refId);
            if (fieldsMap.containsKey(refId)) {
                if (isEqual(inputElementInfo, fieldsMap.get(refId), vm)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                ClassInfo ci = inputElementInfo.getClassInfo();
                for (int j = 0; j < ci.getNumberOfDeclaredInstanceFields(); j++) {
                    FieldInfo fi = ci.getDeclaredInstanceField(j);
                    if (fi.isReference()) {
                        if (!saveElements(inputElementInfo.getFields().getIntValue(fi.getStorageOffset()), vm)) {
                            return false;
                        }
                    }
                }
                fieldsMap.put(refId, inputElementInfo.clone());
                return true;
            }
        }

        public T getCache(int[] input, VM vm, StackFrame stackFrame) {
            if (!inputs.containsKey(Arrays.hashCode(input))) {
                return null;
            }
            int slotsLength = stackFrame.getSlots().length;

            for (int i = 0; i < slotsLength; i++) {
                if (!stackFrame.isReferenceSlot(i)) {
                    continue;
                }
                ElementInfo inputElementInfo= vm.getHeap().get(input[i]);
                if (!isEqual(inputElementInfo, fieldsMap.get(input[i]), vm)) {
                    return null;
                }
            }
            return inputs.get(Arrays.hashCode(input));
        }

        private boolean isEqual(ElementInfo inputElementInfo, ElementInfo localElementInfo, VM vm) {
            ClassInfo ci = inputElementInfo.getClassInfo();
            Fields inputFields = inputElementInfo.getFields();
            Fields localFields = inputElementInfo.getFields();

            for (int j = 0; j < ci.getNumberOfDeclaredInstanceFields(); j++) {
                FieldInfo fi = ci.getDeclaredInstanceField(j);
                if (!fi.isReference()) {
                    if (!fi.getValueObject(inputElementInfo.getFields()).equals(fi.getValueObject(localElementInfo.getFields()))) {
                        System.out.printf("Cache doesn't match: %s \t %s \n",
                                fi.getValueObject(inputElementInfo.getFields()),
                                fi.getValueObject(localElementInfo.getFields()));
                        return false;
                    }
                } else {
                    int inputRef = inputFields.getIntValue(fi.getStorageOffset());
                    int localRef = localFields.getIntValue(fi.getStorageOffset());
                    if (inputRef != localRef) {
                        System.out.println("second false");
                        return false;
                    }
                    if (!isEqual(vm.getHeap().get(inputRef), fieldsMap.get(localRef), vm)) {
                        System.out.println("third false");
                        return false;
                    }
                }
            }
//            System.out.println("true");
            return true;
        }
    }

    public void methodEntered (VM vm, ThreadInfo currentThread, MethodInfo enteredMethod) {
        MethodInfo mi = currentThread.getTopFrameMethodInfo();

        if (mi.getClassName().startsWith("java.") ||
                mi.getClassName().startsWith("gov.") ||
                mi.getClassName().startsWith("sun.")){
            return;
        }

        String key = enteredMethod.getClassName() + ":" + enteredMethod.getName();
        System.out.printf("\n======= %s ======\n", key);
        Integer returnVal;
        if (!methodCacheMap.containsKey(key)) {
            methodCacheMap.put(key, new MethodCache<Integer>());
        }

        MethodCache<Integer> methodCache = methodCacheMap.get(key);

        List<StackFrame> invokedStackFrames = currentThread.getInvokedStackFrames();
        StackFrame invokedStackFrame = invokedStackFrames.get(invokedStackFrames.size() - 1);
        int[] slots = invokedStackFrame.getSlots();

        System.out.printf("Input slots: %s\n", Arrays.toString(slots));

        currentSlot = slots.clone();
        currentStackFrame = invokedStackFrame.clone();

        if (!methodCache.isCached(currentSlot)) {
            return;
        }

        returnVal = methodCache.getCache(slots, vm, invokedStackFrame);
        if (returnVal == null) {
            return;
        }
        System.out.printf("# Found Cached Result: %d #\n", returnVal);

        slots[invokedStackFrame.getTopPos()] = returnVal;

        Instruction curInstruction = enteredMethod.getInstruction(0);
        if (mi.getReturnType().equals("I") ||
                mi.getReturnType().equals("C") ||
                mi.getReturnType().equals("Z")) {
            IRETURN ireturn = new IRETURN();
            ireturn.init(enteredMethod, curInstruction.getInstructionIndex(), curInstruction.getPosition());
            currentThread.getTopFrame().setPC(ireturn);
        } else if (mi.getReturnType().equals("F")) {
            FRETURN freturn = new FRETURN();
            freturn.init(enteredMethod, curInstruction.getInstructionIndex(), curInstruction.getPosition());
            currentThread.getTopFrame().setPC(freturn);
        }
    }

    public void methodExited (VM vm, ThreadInfo currentThread, MethodInfo exitedMethod) {
        String key = exitedMethod.getClassName() + ":" + exitedMethod.getName();
        MethodInfo mi = currentThread.getTopFrameMethodInfo();
        String returnType = mi.getReturnType();

        if (mi.getClassName().startsWith("java.") ||
                mi.getClassName().startsWith("gov.") ||
                mi.getClassName().startsWith("sun.")){
            return;
        }

        if (!(returnType.equals("I") || returnType.equals("Z") || returnType.equals("C") || returnType.equals("F"))) {
            return;
        }

        if (!methodCacheMap.containsKey(key)) {
            methodCacheMap.put(key, new MethodCache<Integer>());
        }

        MethodCache<Integer> methodCache = methodCacheMap.get(key);
        if (currentStackFrame == null) {
            return;
        }

        List<StackFrame> invokedStackFrames = currentThread.getInvokedStackFrames();
        StackFrame invokedStackFrame = invokedStackFrames.get(invokedStackFrames.size() - 1);
        if (invokedStackFrame.getSlots().length > 0) {

            if (methodCache.setCache(currentSlot, invokedStackFrame.peek(), vm, currentStackFrame)) {
                System.out.printf("# %s: Saving result %d #\n", key, invokedStackFrame.peek());
            }
        }


    }
}
