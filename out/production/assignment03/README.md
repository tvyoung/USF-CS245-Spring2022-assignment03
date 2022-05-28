# assignment3-CryptoPortfolio

Vicki Young

2022.05.07

CS 245-03

**NOTES**

- for iterating through a HashMap, used resource: https://stackoverflow.com/questions/1066589/iterate-through-a-hashmap
- for formatting double values without scientific notation, used resource: https://stackoverflow.com/questions/16098046/how-do-i-print-a-double-value-without-scientific-notation-using-java
- when `source` is `btc`, number of cryptocurrencies output is 681
- for `findFlows()`, method ignores cryptocurrencies with edge target pointing to source, and edge weights = 0.0

**QUESTIONS**

**Q1. How does your program find the shortest paths from the source cryptocurrency to others? Explain your algorithm.**

Program uses BFS algorithm with a maxCount variable to find shortest path from given source to every other cryptocurrency possible; maxCount keeps track of the current vertex (cryptocurrency) listed max transaction count, and compares this to its outgoing edge weights in order to find the new max transaction count for its target cryptocurrencies. 

BFS algorithm uses a queue as well as the returned HashMap cryptocurrencies as its list of "visited" cryptocurrencies. 

**Q2. What is the time and space complexity of the shortest-path algorithm in your answer above? Explain your answer.** 

Time: O(|V| + |E|)
V = number of vertices (cryptocurrencies)
E = number of edges 
this is because each vertex is eventually added to queue list, which algorithm goes through until queue is empty. Each vertex is therefore checked, and the algorithm goes through each vertex's edges (while not repeating any target vertices which have already been visited).

SPACE: O(|V|) 
V = number of vertices (cryptocurrencies)
queue will store each vertex (cryptocurrency) once at some point, and returned HashMap cryptocurrencies will store every cryptocurrency and its max transaction count.

**Q3. How does your program sort the (cryptocurrency, net trades count) pairs? Explain your algorithm.**

Algorithm essentially uses a linear search / findLargest algorithm which goes through the given HashMap of (cryptocurrency, net trades count) pairs to find the pair with the max transaction count, saving its cryptocurrency key under maxKey variable. The pair is then removed from HashMap and its cryptocurrency and transaction count are added to String list. Algorithm repeats until HashMap is empty. 

**Q4. What is the time and space complexity of the sorting algorithm in your answer above? Explain your answer.** 

TIME: O(|V|^2)
V = number of vertices (cryptocurrencies)
Algorithm iterates through given HashMap of (cryptocurrency, net trades count) pairs about |V| / 2 times in order to find the greatest transaction count for each iteration. Each iteration depends on number of pairs in HashMap, which decreases by one each iteration due to removing the pair with the greatest transaction count at the end. 

SPACE: O(|V|)
V = number of vertices (cryptocurrencies)
Algorithm returns a String list with the (cryptocurrency, net trades count) pairs listed in descending order. 