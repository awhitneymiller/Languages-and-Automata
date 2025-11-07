# Homework 4
*Audrey Whitney-Miller*
**I have gone through course notes, and the chapters to read.**

## 1. In Java, which keywords (if any) are used to indicate a class may have (a) no instances, (b) only a fixed number of instances, (c) no subclasses, (d) only a fixed number of subclasses.

a. abstract 
- An abstract class cannot be used to create objects.

b. No keyword

c. final
- used to make classes non-changeable, impossible to inherit or override.

d. No keyword

## 2. Very briefly, list the four main differences between a Swift class and a Swift struct.
1) Classes are reference types, Structs are value types.
2) Instances of classes are mutable, instances of structs are immutable (by default)
3) Classes support inheritance, structs do not.
4) Memory for classes is managed by Automatic Reference Counting (ARC), which keeps track of references and deallocates instances when there are no more references. Structs are copied when assigned or passed around.


## 3. Does Swift have null references? If so, show an example. If not, how exactly did they prevent this billion dollar mistake?
Swift has optionals, its solution to null references. An optional is a type that can hold either a value or nil, and Swift forces you to explicitly acknowledge and handle that possibility through optional binding or unwrapping. This is how Swift avoids the “billion-dollar mistake” of unchecked null references.

## 4. Assuming Dog is a subclass of Animal, should you be able to assign an expression of List<Dog> to a variable type constrained as List<Animal> in its declaration? Answer not in terms of what some languages do, but what makes the most sense in terms of type safety.
No, you should not allow a List<Dog> to be assigned to a variable of type List<Animal> if the list is mutable. Even though every dog is an animal, treating a list of dogs as a list of animals breaks type safety because the code holding the List<Animal> reference could insert a different kind of animal, such as a cat, into a structure that is supposed to contain only dogs. This would violate the original list’s element type guarantee and could cause runtime errors when the list is later used as if it still contained only dogs. For full type safety, mutable lists must be invariant. a List<Dog> is not interchangeable with a List<Animal>.

## 5. Why is Swift’s Void type weirdly named? What is their “excuse” for using that term for what is essentially a unit type?
Swift names its return-nothing type Void because it provides a simple alias for the true type (), which is the unit type. The language keeps the name Void so functions can show an empty return value in a familiar way, even though the underlying type is still (). 

## 6. What is the type of a supplier in Swift?
I'm a little stuck on this one, I'm not sure if this is right.
A supplier in Swift has the type () -> T: a function that takes no arguments and returns a value of type T.

## 7. Why did Yegor Bugayenko think Alan Kay was wrong about being wrong about using the term “object” when he coined the term “object-oriented programming”?
Yegor Bugayenko argues that Alan Kay was not actually wrong about the term “object” in object-oriented programming. Kay later said “messaging” might have been a better word, but the author believes that messaging and composition are two different ways objects interact. Messaging keeps objects at the same level and forces them to expose data, which does not solve maintainability problems. Composition, on the other hand, lets you build larger objects out of smaller ones and keep data hidden inside those structures. The author thinks Kay’s point about communication applies to modules in system architecture, not to objects in design. So, in the Bugayenko’s view, Kay was right to call them objects and to name the approach object-oriented programming.

## 8. What is the difference between class-based and prototype-based OOP?
Prototype-based programming offers benefits like easy class creation, multiple inheritance, and runtime behavior changes. However, it sacrifices static typing, incurs performance overhead, and complicates refactoring.
Class-based OOP uses classes as blueprints for objects, with inheritance forming a hierarchical structure where subclasses inherit from parent classes.

## 9. List all the things that a Java record automatically generates.
Java Records automatically generate equals(), hashCode(), and toString() methods based on record fields which simplifies data transfer objects and value types. They also generate a canonical constructor that assigns all components, create private final fields for each component, and provide accessor methods named after each component, giving you a complete immutable data structure with minimal code.

## 10. Java does not (yet?) have companion objects like Kotlin. What do Java programmers have to use instead?
Since Java does not have companion objects like Kotlin, Java programmers rely on static members and static nested classes instead. Static fields and methods belong to the class itself and act as shared data or utilities, while static nested classes let developers group related static behavior in a more organized way. These tools provide some of the structure and grouping that Kotlin companion objects offer, but they are less flexible and less direct to use.
