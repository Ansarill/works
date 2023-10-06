#include "../include/Subset.hpp"

#include "../include/RandomizedQueue.hpp"
void subset(unsigned long k, std::istream& in, std::ostream& out) {
    std::string line;
    RandomizedQueue<std::string> q;
    auto t = k;
    while (t-- > 0 && std::getline(in, line)) {
        q.enqueue(line);
    }
    while (!q.empty() && k-- > 0) {
        out << q.dequeue() << std::endl;
    }
}
