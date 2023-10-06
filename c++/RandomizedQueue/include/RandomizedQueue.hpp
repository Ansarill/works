#ifndef RANDOMIZEDQUEUE_HPP
#define RANDOMIZEDQUEUE_HPP
#include <algorithm>
#include <ctime>
#include <random>
#include <vector>

template <class T>
class RandomizedQueue {
    template <bool is_const_iterator>
    class Iterator;

public:
    typedef Iterator<false> iterator;
    typedef Iterator<true> const_iterator;

    RandomizedQueue()                        = default;
    RandomizedQueue(const RandomizedQueue &) = default;
    RandomizedQueue(RandomizedQueue &&)      = default;
    bool empty() const { return _data.empty(); }
    std::size_t size() const { return _data.size(); }
    void enqueue(const T &x) { _data.push_back(x); }
    void enqueue(T &&x) { _data.emplace_back(std::move(x)); }
    T const &sample() const {
        std::random_device gen;
        std::uniform_int_distribution<> dist(0, size() - 1);
        return _data[dist(gen)];
    }
    T dequeue() {
        std::random_device gen;
        std::uniform_int_distribution<> dist(0, size() - 1);
        std::size_t random_index = dist(gen);
        std::swap(_data[random_index], _data[size() - 1]);
        T deleted = std::move(_data[size() - 1]);
        _data.pop_back();
        return deleted;
    }
    auto begin() { return iterator(_data.begin(), size(), false); };
    auto end() { return iterator(_data.begin(), size(), true); };
    auto begin() const { return const_iterator(_data.begin(), size(), false); };
    auto end() const { return const_iterator(_data.begin(), size(), true); };
    auto cbegin() const { return begin(); };
    auto cend() const { return end(); };

private:
    std::vector<T> _data;
};

template <class T>
template <bool is_const_iterator>
class RandomizedQueue<T>::Iterator {
private:
    friend class RandomizedQueue;
    using pointer_type = std::conditional_t<is_const_iterator, typename std::vector<T>::const_iterator,
                                            typename std::vector<T>::iterator>;
    Iterator(pointer_type start, std::size_t size, bool end) : _start(start), _permutation(size) {
        _current = end ? size : 0;
        for (std::size_t i = 0; i < size; i++) {
            _permutation[i] = i;
        }
        std::random_device gen;
        std::shuffle(_permutation.begin(), _permutation.end(), std::mt19937_64(gen()));
        _permutation.push_back(size);
    }
    pointer_type _start;
    std::size_t _current;
    std::vector<std::size_t> _permutation;

public:
    using value_type        = std::conditional_t<is_const_iterator, std::add_const_t<T>, T>;
    using difference_type   = std::ptrdiff_t;
    using iterator_category = std::forward_iterator_tag;
    using pointer           = value_type *;
    using reference         = value_type &;
    Iterator() : _start(nullptr), _current(0){};
    template <bool const_it>
    Iterator(const Iterator<const_it> &other)
        : _start(other._start), _current(other._current), _permutation(other._permutation) {}
    Iterator &operator++() {
        ++_current;
        return *this;
    }
    Iterator operator++(int) {
        auto to_return(*this);
        ++_current;
        return to_return;
    }
    bool operator!=(const Iterator &other) const {
        return (_start + _permutation[_current]) != (other._start + other._permutation[other._current]);
    }
    bool operator==(const Iterator &other) const { return !(operator!=(other)); }
    reference operator*() const { return *(_start + _permutation[_current]); }
    pointer operator->() const { return &(_start + _permutation[_current]); }
};
#endif  // RANDOMIZEDQUEUE_HPP
