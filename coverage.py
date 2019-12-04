
def convert_coverage(filename):
    with open('out/{}.txt'.format(filename)) as f:
        lines = [line.strip().split(',') for line in f.readlines()]
    
    with open('out/testPath.txt') as f:
        path = f.readlines()[0].strip().split(',')
    
    def is_covered(line, path):
        for i in range(len(path)):
            for j in range(len(line)):
                if i + j >= len(path) or line[j] != path[i + j]:
                    break
            else:
                return True
        return False
    
    
    
    coverage = []
    
    for line in lines:
        if is_covered(line, path):
            print(line)
            coverage.append(','.join(line) + '\n')

    with open('out/{}_coverage.txt'.format(filename), 'w') as f:
        print(coverage)
        f.writelines(coverage)

convert_coverage('nodes')
convert_coverage('edges')
convert_coverage('edgePairs')
convert_coverage('simplePaths')
convert_coverage('primePaths')


