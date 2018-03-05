# CS 6380: Distributed Computing

# Section 001

# Project 1

You can work on this programming project either individually or in a group. A group can
contain up to three students. Code sharing among group is strictly prohibited and will result in
disciplinary action being taken.
You can do this project in C, C++ or Java. Each student is expected to demonstrate the
operation of this project to the instructor or the TA. Since the project involves socket programming,
you can only use machinesdcXX.utdallas.edu, whereXX∈ {01, 02, .., 45}, for running the program.
Although you may develop the project on any platform, the demonstration has to be ondcXX
machines; otherwise, you will be assessed a penalty of 20%.

## 1 Project Description

This project consists of four parts: (a) build a message-passing synchronous distributed system
in which nodes are arranged in a certain topology (given in a configuration file), (b) implement
Peleg’s leader election algorithm to elect a leader, (c) build a breadth first search (BFS) spanning
tree rooted at the leader, and (d) use the BFS tree to find the maximum degree of any node in the
BFS tree(not the network).
You can assume that all links are bidirectional. You will need to use a synchronizer to simulate
a synchronous system. Details of a simple synchronizer will be discussed in the class.

Output: Each node should print the following to the screen when appropriate: (i) UIDs of its
parent and children nodes in the BFS tree, and (ii) its degree in the BFS tree. In addition, the
leader also prints the maximum degree of any node in the BFS tree.

## 2 Sample Configuration File

\# Configuration file for CS 6380 Project 1 (Spring 2018)

\#

\# As per the "shell" convention, anything following a hash sign is

\# a comment and should be ignored by the parser.

\#

\# Number of nodes

5

\# Here we list the individual nodes

\#

\# Format is:

\# UID Hostname Port UIDs of Neighbors

123 127.0.0.1 50000

5 127.0.0.1 50001

23 127.0.0.1 50002

1043 127.0.0.1 50003 

89 127.0.0.1 50004

\# Node#   Neighbors

5      123 1043

123    5 23

23     123 1043 89

1043   5 23 89

89     23 1043

## 3 How To Run The Project
1. Place config.txt at the same directory as jar file or the compliled Node.class file
2. Run the jar File

```
1. User should modify the config.txt accordingly to configure port, ip and neighbors for each node. The sample config file is given here.

2. When you run the program, you should also input the node id separately. Eg: when you start machine one with node id 1, 
you should use java -jar **.jar 1

3. The SynchBFS folder contians implementation of Synchronous BFS tree, while the other is implementation asynchronous spanning tree.
My Email is wxl163530@gmail.com, feel free to contact me about the project.
```