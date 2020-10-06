import argparse

import networkx as nx
import matplotlib.pyplot as plt


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('alt_file', type=argparse.FileType(
        'r'), metavar='<alt file>')
    args = parser.parse_args()

    graph = nx.Graph()
    d = {}
    vertices = False
    edges = False
    pos = {}

    for line in args.alt_file:
        w = line.strip().split()
        if "VERTICES" in w:
            vertices = True
            edges = False
        if "EDGES" in w:
            vertices = False
            edges = True
        if len(w) == 4:
            a, b, x, y = line.strip().split()
            if vertices == True:
                d[a] = b
                pos[a] = (int(x), int(y))
            if edges == True:
                graph.add_edge(d[a], d[b])

    # print(pos)
    nx.draw_networkx(graph, node_size=30, width=0.00001, font_size=2, pos=pos)
    plt.savefig(args.alt_file.name.split(".")[0] + '.eps')


if __name__ == '__main__':
    main()
