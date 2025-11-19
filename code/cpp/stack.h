#ifndef STACK_H
#define STACK_H

#include <stdexcept>
#include <string>
#include <memory>
#include <utility>

#define MAX_CAPACITY 32768
#define INITIAL_CAPACITY 16

template <typename T>
class Stack {
  // Allocate the data array with a smart pointer, so no destructor needed
  std::unique_ptr<T[]> elements;
  int capacity;
  int top;   // number of elements currently stored

  // Prohibit copying and assignment (and moving)
  Stack(const Stack&) = delete;
  Stack& operator=(const Stack&) = delete;
  Stack(Stack&&) = delete;
  Stack& operator=(Stack&&) = delete;

public:
  // Default constructor
  Stack()
    : elements(std::make_unique<T[]>(INITIAL_CAPACITY)),
      capacity(INITIAL_CAPACITY),
      top(0) {}

  // Current number of stored elements
  int size() const {
    return top;
  }

  bool is_empty() const {
    return top == 0;
  }

  bool is_full() const {
    return top == MAX_CAPACITY;
  }

  // Push a copy of item onto the stack
  void push(const T& item) {
    if (top == MAX_CAPACITY) {
      throw std::overflow_error("Stack has reached maximum capacity");
    }

    if (top == capacity) {
      int new_capacity = capacity * 2;
      if (new_capacity > MAX_CAPACITY) {
        new_capacity = MAX_CAPACITY;
      }
      reallocate(new_capacity);
    }

    elements[top] = item;  // copy
    ++top;
  }

  // Pop and return the top element (by value)
  T pop() {
    if (top == 0) {
      throw std::underflow_error("cannot pop from empty stack");
    }

    // Copy value out before mutating
    T value = elements[top - 1];
    --top;

    // Shrink when usage drops below 1/4 of capacity, but not below INITIAL_CAPACITY
    if (capacity > INITIAL_CAPACITY && top < capacity / 4) {
      int new_capacity = capacity / 2;
      if (new_capacity < INITIAL_CAPACITY) {
        new_capacity = INITIAL_CAPACITY;
      }
      reallocate(new_capacity);
    }

    return value;
  }

private:
  // Resize underlying storage, preserving existing elements
  void reallocate(int new_capacity) {
    if (new_capacity < INITIAL_CAPACITY) {
      new_capacity = INITIAL_CAPACITY;
    }
    if (new_capacity > MAX_CAPACITY) {
      new_capacity = MAX_CAPACITY;
    }
    if (new_capacity == capacity) {
      return;
    }

    std::unique_ptr<T[]> new_elements = std::make_unique<T[]>(new_capacity);

    // Copy current elements into new storage
    for (int i = 0; i < top; ++i) {
      new_elements[i] = elements[i];
    }

    elements = std::move(new_elements);
    capacity = new_capacity;
  }
};

#endif // STACK_H
