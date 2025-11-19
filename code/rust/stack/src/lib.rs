pub struct Stack<T> {
    elements: Vec<T>,
}

impl<T> Stack<T> {
    /// Create a new empty stack
    pub fn new() -> Self {
        Stack {
            elements: Vec::new(),
        }
    }

    /// Current number of elements
    pub fn len(&self) -> usize {
        self.elements.len()
    }

    /// Is the stack empty?
    pub fn is_empty(&self) -> bool {
        self.elements.is_empty()
    }

    /// Push a value onto the stack
    pub fn push(&mut self, value: T) {
        self.elements.push(value);
    }

    /// Pop the top value off the stack, or None if empty
    pub fn pop(&mut self) -> Option<T> {
        self.elements.pop()
    }

    /// Peek at the top value by reference, or None if empty
    pub fn peek(&self) -> Option<&T> {
        self.elements.last()
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_push_and_pop() {
        let mut stack: Stack<i32> = Stack::new();
        assert!(stack.is_empty());
        stack.push(1);
        stack.push(2);
        assert_eq!(stack.len(), 2);
        assert_eq!(stack.pop(), Some(2));
        assert_eq!(stack.pop(), Some(1));
        assert_eq!(stack.pop(), None);
        assert!(stack.is_empty());
    }

    #[test]
    fn test_peek() {
        let mut stack: Stack<i32> = Stack::new();
        assert_eq!(stack.peek(), None);
        stack.push(3);
        assert_eq!(stack.peek(), Some(&3));
        stack.push(5);
        assert_eq!(stack.peek(), Some(&5));
    }

    #[test]
    fn test_is_empty() {
        let mut stack: Stack<String> = Stack::new();
        assert!(stack.is_empty());
        stack.push(String::from("hello"));
        assert!(!stack.is_empty());
        stack.pop();
        assert!(stack.is_empty());
    }

    #[test]
    fn test_stacks_cannot_be_cloned_or_copied() {
        let stack1: Stack<i32> = Stack::new();
        let _stack2: Stack<i32> = stack1;
        // Should get a compile error if next line uncommented
        // let _stack3: Stack<i32> = stack1; // Error: `stack1` has been moved
    }
}
