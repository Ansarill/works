#ifndef WORDNET_WORDNET_HPP
#define WORDNET_WORDNET_HPP

#include <iosfwd>
#include <iostream>
#include <queue>
#include <set>
#include <sstream>
#include <string>
#include <unordered_map>
#include <unordered_set>
#include <vector>

class Digraph {
public:
    Digraph() = default;
    Digraph(std::unordered_map<unsigned, std::vector<unsigned>>&& adj);
    std::vector<unsigned> get_adjacent(unsigned id) const;
    std::unordered_map<unsigned, unsigned> bfs(unsigned start) const;
    unsigned size() const;
    std::ostream& print(std::ostream& out);

private:
    std::unordered_map<unsigned, std::vector<unsigned>> _adj;
};

class ShortestCommonAncestor {
public:
    explicit ShortestCommonAncestor(Digraph& dg);
    explicit ShortestCommonAncestor(const Digraph& dg);
    unsigned length(unsigned v, unsigned w);
    unsigned ancestor(unsigned v, unsigned w);
    unsigned length_subset(const std::set<unsigned>& subset_a, const std::set<unsigned>& subset_b);
    unsigned ancestor_subset(const std::set<unsigned>& subset_a, const std::set<unsigned>& subset_b);

private:
    std::pair<unsigned, unsigned> shortest_finder(unsigned v, unsigned w);
    std::pair<unsigned, unsigned> shortest_finder_subset(const std::set<unsigned>& subset_a,
                                                         const std::set<unsigned>& subset_b);
    std::unordered_map<unsigned, unsigned> bfs(unsigned start);
    const Digraph& _dg;
};

class WordNet {
public:
    WordNet(std::istream& synsets, std::istream& hypernyms);

    class Nouns {
        friend WordNet;

    public:
        class iterator {
            friend Nouns;

        public:
            using iterator_category = std::forward_iterator_tag;
            using difference_type   = unsigned;
            using value_type        = std::string;
            using pointer           = const value_type*;
            using reference         = const value_type&;
            using pointer_type      = std::vector<value_type>::const_iterator;
            iterator();
            iterator& operator++();
            iterator operator++(int);
            bool operator==(iterator const& other) const;
            bool operator!=(iterator const& other) const;
            reference operator*() const;
            pointer operator->() const;

        private:
            pointer_type _begin;
            unsigned _current;
            iterator(pointer_type begin, unsigned index);
        };

        iterator begin() const;
        iterator end() const;

    private:
        Nouns(const std::vector<std::string>& nouns);
        std::vector<std::string> const& _nouns;
    };

    Nouns nouns() const;

    bool is_noun(const std::string& word) const;

    std::string sca(const std::string& noun1, const std::string& noun2) const;

    unsigned distance(const std::string& noun1, const std::string& noun2) const;

private:
    Digraph _graph;
    std::unordered_map<unsigned, std::string> _gloss;
    std::unordered_map<std::string, std::set<unsigned>> _synsets_of_noun;
    std::vector<std::string> _nouns;
};

class Outcast {
public:
    explicit Outcast(WordNet& wordnet);

    std::string outcast(const std::set<std::string>& nouns);

private:
    WordNet& _wordnet;
};

#endif  // WORDNET_WORDNET_HPP
