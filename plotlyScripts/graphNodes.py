import sys
import plotly.plotly as py
import plotly.graph_objs as go

filePathToLoad = sys.argv[1]

#expects nodes as strings of form (x,y)
def readNodePair(strungNodePair, depth):
    splitNum = str.split(strungNodePair, ',')
    firstNum = int((splitNum[0][1::])) #everything before the comma except the parenthesis.
    secondNum = int(splitNum[1][:-1:]) #everything after the comma except the parenthesis.
    return tuple([firstNum, secondNum, -depth]) #return the data as data.

#creating the dictionaries for (node,frequency) and (edge,frequency) for rendering on a color scale.
nodeFrequencyDict = dict()
edgeFrequencyDict = dict()
maximumSolutionLength = 0

for line in open(filePathToLoad, 'r'):
    nodes = str.split(line, ' ')
    numNodes = len(nodes)
    #the last node of the line has a newline character we need to remove.
    nodes[numNodes-1] = nodes[numNodes-1][:-1]

    for i in range(len(nodes)): #every node needs to be rendered
        maximumSolutionLength = max(maximumSolutionLength, len(nodes))
        node1 = readNodePair(nodes[i], i)
        if node1 not in nodeFrequencyDict:
            nodeFrequencyDict[node1] = 1
        else:
            nodeFrequencyDict[node1] = nodeFrequencyDict[node1] + 1
        if i < len(nodes) - 1: #every pair of nodes we read needs to have an edge drawn between it
            node2 = readNodePair(nodes[i+1], i+1)
            edge = tuple([node1,node2])
            if edge not in edgeFrequencyDict:
                edgeFrequencyDict[edge] = 1
            else: #if the edge already exists, simply increment it.
                edgeFrequencyDict[edge] = edgeFrequencyDict[edge] + 1

# print(nodeFrequencyDict)
# print()
# print(edgeFrequencyDict)

#creating some plot.ly objects
edge_trace = go.Scatter3d(
    x=[],
    y=[],
    z=[],
    line=dict(
        width=3,
        color=[],
        colorscale='RdBu',
        ),
    hoverinfo='none',
    mode='lines')

# stuffing our objects into plot.ly format.
# edge_track expects two arrays each containing tuples of 3(?) objects: edge_trace['x'] += tuple([x0, x1, None])

for edge, frequency in edgeFrequencyDict.items():
    edge_trace['x'] += tuple([edge[0][0], edge[1][0]])
    edge_trace['y'] += tuple([edge[0][1], edge[1][1]])
    edge_trace['z'] += tuple([edge[0][2], edge[1][2]])
    edge_trace['line']['color']+= tuple([frequency])

node_trace = go.Scatter3d(
    x=[],
    y=[],
    z=[],
    text=[],
    mode='markers',
    hoverinfo='all',
    marker=dict(
        # colorscale options
        #'Greys' | 'YlGnBu' | 'Greens' | 'YlOrRd' | 'Bluered' | 'RdBu' |
        #'Reds' | 'Blues' | 'Picnic' | 'Rainbow' | 'Portland' | 'Jet' |
        #'Hot' | 'Blackbody' | 'Earth' | 'Electric' | 'Viridis' |
        colorscale='RdBu',
        color=[],
        size=10,
        colorbar=dict(
            thickness=15,
            title='Node Connections',
            xanchor='left',
            titleside='right'
        ),
        ))

#node_trace['x'] += tuple([x])
for node, frequency in nodeFrequencyDict.items():
    node_trace['x'] += tuple([node[0]])
    node_trace['y'] += tuple([node[1]])
    node_trace['z'] += tuple([node[2]])
    node_trace['marker']['color']+= tuple([frequency])
    node_info = '# of connections: ' + str(frequency)
    node_trace['text']+=tuple([node_info])

fileName = str.split(filePathToLoad, '/')
fileName = fileName[len(fileName) - 1]
numFloors = int(str.split(fileName, '-')[1])
numEggs = int(str.split(fileName, '-')[2][:-4])

layout=go.Layout(
    title='<br>All Egg Drop Solutions for ' + str(numFloors) + ' floors and ' + str(numEggs) + " eggs.",
    titlefont=dict(size=16),
    showlegend=False,
    hovermode='closest',
    scene = dict(
    xaxis=dict(
        range=[0.5,numFloors + 0.5],
        title='Number of Floors Remaining',
        tick0=0,
        tickwidth=1
        ),
    yaxis=dict(
        range=[0.5,numEggs + 0.5],
        title='Number of Eggs Remaining',
        tick0=0,
        tickwidth=1
        ),
    zaxis=dict(
        range=[-maximumSolutionLength, 0.5],
        title='Number of Drops',
        tick0=0,
        tickwidth=1
        )
    ),
    width=700,
    margin=dict(
        r=10, l=10,
        b=10, t=10)
)
fig = go.Figure(data=[node_trace, edge_trace], layout=layout)
py.plot(fig, filename=fileName)
