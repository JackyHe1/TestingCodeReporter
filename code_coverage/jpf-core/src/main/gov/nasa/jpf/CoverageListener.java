/*
 * Copyright (C) 2014, United States Government, as represented by the
 * Administrator of the National Aeronautics and Space Administration.
 * All rights reserved.
 *
 * The Java Pathfinder core (jpf-core) platform is licensed under the
 * Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0. 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package gov.nasa.jpf;

import gov.nasa.jpf.vm.*;

import java.util.*;

/**
 * this is just a common root type for VMListeners and SearchListeners. No
 * own interface, just a type tag. It's main purpose is to provide some 
 * typechecks during config-based reflection instantiation
 */
public class CoverageListener extends ListenerAdapter {
    Line head = new Line(null, null, null);
    Line cur = head;

    private class Line {
        Integer lineNum;
        String command;
        String file;
        Line next;

        public Line(Integer lineNum, String command, String file) {
            this.lineNum = lineNum;
            this.command = command;
            this.file = file;
        }

        public void setNext(Line next) {
            this.next = next;
        }

        @Override
        public int hashCode() {
            return this.lineNum;
        }

        @Override
        public boolean equals(Object obj) {
            return this.lineNum.equals(((Line) obj).lineNum)
                    && this.file.equals(((Line) obj).file);
        }
    }

    HashMap<String, HashSet<Line>> coverage = new HashMap<>();


    public void executeInstruction(VM vm, ThreadInfo currentThread, Instruction instructionToExecute) {
        MethodInfo mi = currentThread.getTopFrameMethodInfo();
        if (mi.getClassName().startsWith("java.") ||
                mi.getClassName().startsWith("gov.") ||
                mi.getClassName().startsWith("sun.") ||
                instructionToExecute.getLineNumber() <= 0){
            return;
        }
        if (!coverage.containsKey(mi.getSourceFileName())) {
            coverage.put(mi.getSourceFileName(), new HashSet<>());
        }
        instructionToExecute.getLineNumber();
        Line line = new Line(
                instructionToExecute.getLineNumber(),
                instructionToExecute.getSourceLine(),
                mi.getSourceFileName());
        if (!line.equals(cur)) {
            cur.setNext(line);
            cur = line;
        }
        coverage.get(mi.getSourceFileName()).add(line);
    }

    @Override
    public void threadTerminated(VM vm, ThreadInfo terminatedThread) {
        System.out.printf("\n============== Coverage ==============\n");
        coverage.forEach((k, v) -> {
            System.out.printf("\n------------ %s ------------\n", k);
            List<Line> lines = new ArrayList<>(v);
            Collections.sort(lines, Comparator.comparing((line -> line.lineNum)));
            for (Line line: lines) {
//                System.out.printf("%-5d: %s\n", line.lineNum, line.command);
            }
        });
        Line line = head.next;
        while (line != null) {
            System.out.printf("%-5d: %s\n", line.lineNum, line.command);
            line = line.next;
        }
    }
}
