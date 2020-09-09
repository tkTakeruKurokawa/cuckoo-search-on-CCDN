import argparse

import networkx as nx
import matplotlib.pyplot as plt


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('alt_file', type=argparse.FileType('r'), metavar='<alt file>')
    args = parser.parse_args()

    graph = nx.Graph()
    d = {}
    vertices = False
    edes = False

    for line in args.alt_file:
        w = line.strip().split()
        if "VERTICES" in w:
            vertices = True
            edes = False
        if "EDGES" in w:
            vertices = False
            edes = True
        if len(w) == 4:
            a, b, _, _ = line.strip().split()
            if vertices == True:
                d[a] = b
            if edes == True:
                graph.add_edge(d[a], d[b])

    nx.draw_networkx(graph, node_size=3, width=0.1, font_size=1)
    plt.savefig('graph.eps')


if __name__ == '__main__':
    main()