For the refactoring, I changed how the board is set up. This has several parts: 

First, a centralized BoardBuilder class is created, to handle all board initialization, which includes
methods using full specification, default/non-default values, and random values. 

Second, I created NeighborConnection and related classes. These classes handle detailed neighbor connections. 
For example, connections can be 4-connection (only adjacent), 8-connection (both adjacent and corner), and
arbitrary connection (subset of 8-connection). 

Last, the way to create those connections are further refactored by having three methods that create adjacent
neighbor connection, corner neighbor connection, and arbitrary neighbor connection. All the three methods support
wrap around at borders, which can also be used to create torsoidal board pattern. 
