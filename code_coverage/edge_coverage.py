import networkx as nx
import matplotlib.pyplot as plt

def get_edges():
    with open('edges.txt') as f:
        edges = f.readlines()

    return [edge.strip().split(',') for edge in edges]

def get_paths():
    with open('paths.txt') as f:
        paths = f.readlines()
    return [path.strip().split(',') for path in paths]
    
def edge_coverage():
    edges = get_edges()
    paths = get_paths()
    path = paths[0]

    graph = nx.DiGraph()
    graph.add_edges_from(edges)

    covered = set()
    for i in range(len(path) - 1):
        covered.add((path[i], path[i+1]))
    print(edges)
    print(graph.edges)

    edge_colors = []
    for edge in graph.edges:
        if edge in covered:
            edge_colors.append('salmon')
        else:
            edge_colors.append('skyblue')
    
    node_colors = []
    for node in graph.nodes:
        node_colors.append('silver')

    plt.figure(dpi=1200)
    nx.draw_networkx(graph, node_color=node_colors, edge_color=edge_colors)
    plt.savefig("path.png")
    plt.show()

edge_coverage()



