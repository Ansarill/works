#include "../include/Wordnet.hpp"

std::ostream& operator<<(std::ostream& out, Digraph g) {
    return g.print(out);
}

std::ostream& Digraph::print(std::ostream& out) {
    for (const auto& el : _adj) {
        unsigned id = el.first;
        out << id;
        const auto& hypernyms = el.second;
        if (el.second.size() > 0) {
            out << "->";
            for (unsigned id_h : hypernyms) {
                out << id_h << " ";
            }
        }
        out << "\n";
    }
    return out;
}

Digraph::Digraph(std::unordered_map<unsigned, std::vector<unsigned>>&& adj) : _adj(adj) {}

std::vector<unsigned> Digraph::get_adjacent(unsigned id) const {
    return _adj.at(id);
}
std::unordered_map<unsigned, unsigned> Digraph::bfs(unsigned start) const {
    std::unordered_map<unsigned, unsigned> dist;
    std::queue<unsigned> q;
    dist[start] = 0;
    q.push(start);
    while (!q.empty()) {
        unsigned u = q.front();
        q.pop();
        for (const unsigned& x : _adj.at(u)) {
            bool was_here = dist.count(x);
            dist[x]       = (!was_here ? dist[u] + 1 : std::min(dist[x], dist[u] + 1));
            q.push(x);
        }
    }
    return dist;
}
unsigned Digraph::size() const {
    return _adj.size();
}

ShortestCommonAncestor::ShortestCommonAncestor(Digraph& dg) : _dg(dg) {}
ShortestCommonAncestor::ShortestCommonAncestor(const Digraph& dg) : _dg(dg) {}
unsigned ShortestCommonAncestor::length(unsigned v, unsigned w) {
    return shortest_finder(v, w).second;
}
unsigned ShortestCommonAncestor::ancestor(unsigned v, unsigned w) {
    return shortest_finder(v, w).first;
}
unsigned ShortestCommonAncestor::length_subset(const std::set<unsigned>& subset_a, const std::set<unsigned>& subset_b) {
    return ShortestCommonAncestor::shortest_finder_subset(subset_a, subset_b).second;
}
unsigned ShortestCommonAncestor::ancestor_subset(const std::set<unsigned>& subset_a,
                                                 const std::set<unsigned>& subset_b) {
    return shortest_finder_subset(subset_a, subset_b).first;
}
std::pair<unsigned, unsigned> ShortestCommonAncestor::shortest_finder(unsigned v, unsigned w) {
    auto dist_v             = bfs(v);
    auto dist_w             = bfs(w);
    unsigned min_len        = static_cast<unsigned>(-1);
    unsigned short_ancestor = static_cast<unsigned>(-1);
    for (const auto& el : dist_v) {
        unsigned ancestor = el.first;
        if (dist_w.count(ancestor)) {
            unsigned dist_to_i = el.second + dist_w[ancestor];
            if (min_len > dist_to_i) {
                short_ancestor = ancestor;
                min_len        = dist_to_i;
            }
        }
    }
    return {short_ancestor, min_len};
}
std::pair<unsigned, unsigned> ShortestCommonAncestor::shortest_finder_subset(const std::set<unsigned>& subset_a,
                                                                             const std::set<unsigned>& subset_b) {
    unsigned min_len        = static_cast<unsigned>(-1);
    unsigned short_ancestor = static_cast<unsigned>(-1);
    for (unsigned a : subset_a) {
        auto dist_a = bfs(a);
        for (unsigned b : subset_b) {
            auto dist_b = bfs(b);
            for (const auto& el : dist_a) {
                unsigned ancestor = el.first;
                if (dist_b.count(ancestor)) {
                    unsigned dist_to_i = el.second + dist_b[ancestor];
                    if (min_len > dist_to_i) {
                        min_len        = dist_to_i;
                        short_ancestor = ancestor;
                    }
                }
            }
        }
    }
    return {short_ancestor, min_len};
}
std::unordered_map<unsigned, unsigned> ShortestCommonAncestor::bfs(unsigned start) {
    return _dg.bfs(start);
}

WordNet::WordNet(std::istream& synsets, std::istream& hypernyms) {
    std::string line;
    std::unordered_map<unsigned, std::vector<unsigned>> adjacent;
    while (std::getline(synsets, line)) {
        if (line.size() == 0) {
            continue;
        }
        std::stringstream line_stream(line);

        std::string id_string;
        std::getline(line_stream, id_string, ',');
        unsigned id  = std::stoi(id_string);
        adjacent[id] = {};
        std::string synset;
        std::getline(line_stream, synset, ',');
        std::stringstream synset_stream(synset);
        std::string word;
        while (std::getline(synset_stream, word, ' ')) {
            _synsets_of_noun[word].insert(id);
        }
        std::string gloss;
        std::getline(line_stream, gloss);
        _gloss[id] = gloss;
    }
    for (const auto& el : _synsets_of_noun) {
        _nouns.push_back(el.first);
    }

    while (std::getline(hypernyms, line)) {
        if (line.size() == 0) {
            continue;
        }
        std::stringstream line_stream(line);
        std::string id_string;
        std::getline(line_stream, id_string, ',');
        unsigned id = std::stoi(id_string);
        std::string hypernym;
        while (std::getline(line_stream, hypernym, ',')) {
            adjacent[id].push_back(std::stoi(hypernym));
        }
    }
    _graph = {std::move(adjacent)};
}

WordNet::Nouns::iterator::iterator() : _begin(nullptr), _current(0) {}

WordNet::Nouns::iterator& WordNet::Nouns::iterator::operator++() {
    ++_current;
    return *this;
}

WordNet::Nouns::iterator WordNet::Nouns::iterator::operator++(int) {
    auto to_return(*this);
    _current++;
    return to_return;
}
bool WordNet::Nouns::iterator::operator==(iterator const& other) const {
    return (_begin + _current) == (other._begin + other._current);
}
bool WordNet::Nouns::iterator::operator!=(iterator const& other) const {
    return !(this->operator==(other));
}
WordNet::Nouns::iterator::reference WordNet::Nouns::iterator::operator*() const {
    return *(_begin + _current);
}
WordNet::Nouns::iterator::pointer WordNet::Nouns::iterator::operator->() const {
    return &*(_begin + _current);
}
WordNet::Nouns::iterator::iterator(pointer_type begin, unsigned index) : _begin(begin), _current(index) {}

WordNet::Nouns::iterator WordNet::Nouns::begin() const {
    return iterator(_nouns.cbegin(), 0);
}
WordNet::Nouns::iterator WordNet::Nouns::end() const {
    return iterator(_nouns.cbegin(), _nouns.size());
}

WordNet::Nouns WordNet::nouns() const {
    return Nouns{_nouns};
}

WordNet::Nouns::Nouns(const std::vector<std::string>& nouns) : _nouns(nouns) {}

bool WordNet::is_noun(const std::string& word) const {
    return _synsets_of_noun.count(word) > 0;
}

std::string WordNet::sca(const std::string& noun1, const std::string& noun2) const {
    if (_synsets_of_noun.count(noun1) == 0 || _synsets_of_noun.count(noun2) == 0)
        return "";
    const std::set<unsigned>& subset_1 = _synsets_of_noun.at(noun1);
    const std::set<unsigned>& subset_2 = _synsets_of_noun.at(noun2);
    ShortestCommonAncestor solve{_graph};
    return _gloss.at(solve.ancestor_subset(subset_1, subset_2));
}
unsigned WordNet::distance(const std::string& noun1, const std::string& noun2) const {
    if (_synsets_of_noun.count(noun1) == 0 || _synsets_of_noun.count(noun2) == 0)
        return 0;
    const std::set<unsigned>& subset_1 = _synsets_of_noun.at(noun1);
    const std::set<unsigned>& subset_2 = _synsets_of_noun.at(noun2);
    ShortestCommonAncestor solve{_graph};
    return solve.length_subset(subset_1, subset_2);
}

Outcast::Outcast(WordNet& wordnet) : _wordnet(wordnet) {}

std::string Outcast::outcast(const std::set<std::string>& nouns) {
    unsigned max_d1        = 0;
    unsigned max_d2        = 0;
    unsigned min_d         = static_cast<unsigned>(-1);
    std::string const* ans = nullptr;
    for (const auto& s : nouns) {
        unsigned d_i = 0;
        for (const auto& s2 : nouns) {
            if (s != s2) {
                d_i += _wordnet.distance(s, s2);
            }
        }
        if (d_i > max_d1) {
            max_d1 = d_i;
            ans    = &s;
        } else {
            max_d2 = std::max(max_d2, d_i);
        }
        if (d_i < min_d) {
            min_d = d_i;
        }
    }
    if (max_d1 != max_d2 && max_d1 > min_d && nouns.size() > 0 && ans && _wordnet.is_noun(*ans)) {
        return *ans;
    } else {
        return "";
    }
}
