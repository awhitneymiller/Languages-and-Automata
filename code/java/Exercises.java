import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Exercises {

    // helper class
    public static <A, B> Optional<B> firstThenApply(
            A[] arr,
            java.util.function.Predicate<A> pred,
            java.util.function.Function<A, B> transform
    ) {
        return Arrays.stream(arr)
            .filter(pred)
            .findFirst()
            .map(transform);
    }

    // exercise 1
    public static Optional<String> firstThenLowerCase(
            List<String> list,
            Predicate<String> pred
    ) {
        return list.stream()
            .filter(pred)
            .findFirst()
            .map(s -> s.toLowerCase());
    }

    // exercise 2
    public static class PhraseChain {
        private final String phrase;

        private PhraseChain(String phrase) {
            this.phrase = phrase;
        }

        public PhraseChain and(String next) {
            return new PhraseChain(this.phrase + " " + next);
        }

        public String phrase() {
            return this.phrase;
        }
    }

    // say() -> empty phrase
    public static PhraseChain say() {
        return new PhraseChain("");
    }

    // say("hi") -> starting phrase "hi"
    public static PhraseChain say(String first) {
        return new PhraseChain(first);
    }

    // exercise 3
    // meaningfulLineCount: count lines that are not blank and not starting with '#'
    public static int meaningfulLineCount(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            return (int) reader.lines()
                .filter(line -> {
                    String trimmed = line.trim();
                    return !(trimmed.isEmpty() || trimmed.startsWith("#"));
                })
                .count();
        }
    }
}

// exercise 4
record Quaternion(double a, double b, double c, double d) {

    // constants
    public static final Quaternion ZERO = new Quaternion(0.0, 0.0, 0.0, 0.0);
    public static final Quaternion I    = new Quaternion(0.0, 1.0, 0.0, 0.0);
    public static final Quaternion J    = new Quaternion(0.0, 0.0, 1.0, 0.0);
    public static final Quaternion K    = new Quaternion(0.0, 0.0, 0.0, 1.0);

    
    public Quaternion {
        if (Double.isNaN(a) || Double.isNaN(b) || Double.isNaN(c) || Double.isNaN(d)) {
            throw new IllegalArgumentException("Coefficients cannot be NaN");
        }
    }

    // plus: component-wise add
    public Quaternion plus(Quaternion other) {
        return new Quaternion(
            this.a + other.a,
            this.b + other.b,
            this.c + other.c,
            this.d + other.d
        );
    }

    // times: quaternion multiply
    public Quaternion times(Quaternion other) {
        double A = this.a, B = this.b, C = this.c, D = this.d;
        double E = other.a, F = other.b, G = other.c, H = other.d;
        return new Quaternion(
            A * E - B * F - C * G - D * H,
            A * F + B * E + C * H - D * G,
            A * G - B * H + C * E + D * F,
            A * H + B * G - C * F + D * E
        );
    }

    // coefficients: return [a,b,c,d] as a List<Double>
    public List<Double> coefficients() {
        return List.of(a, b, c, d);
    }

    // conjugate: negate i,j,k parts
    public Quaternion conjugate() {
        return new Quaternion(a, -b, -c, -d);
    }

    // helper for toString(): append + or - and format i/j/k terms
    private static void appendComponent(StringBuilder sb,
                                        double coef,
                                        String symbol,
                                        boolean isFirstTerm) {
        if (coef == 0.0) {
            return;
        }

        boolean neg = coef < 0;
        double abs = Math.abs(coef);

        if (isFirstTerm) {
            if (neg) sb.append("-");
        } else {
            if (neg) {
                sb.append("-");
            } else {
                sb.append("+");
            }
        }

        boolean isUnit = (abs == 1.0);
        if (!isUnit) {
            sb.append(Double.toString(abs));
        }

        sb.append(symbol);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // real (a) first if nonzero
        if (a != 0.0) {
            sb.append(Double.toString(a));
        }

        // then b i, c j, d k
        appendComponent(sb, b, "i", sb.length() == 0);
        appendComponent(sb, c, "j", sb.length() == 0);
        appendComponent(sb, d, "k", sb.length() == 0);

        if (sb.length() == 0) {
            return "0";
        }

        return sb.toString();
    }
}

// exercise 5
sealed interface BinarySearchTree permits Empty, Node {
    BinarySearchTree insert(String value);
    boolean contains(String value);
    int size();
}

// Empty tree
final class Empty implements BinarySearchTree {

    public Empty() {}

    @Override
    public BinarySearchTree insert(String value) {
        return new Node(value, new Empty(), new Empty());
    }

    @Override
    public boolean contains(String value) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    // Standalone empty tree prints "()"
    @Override
    public String toString() {
        return "()";
    }
}

// Non-empty node
final class Node implements BinarySearchTree {
    private final String value;
    private final BinarySearchTree left;
    private final BinarySearchTree right;

    public Node(String value, BinarySearchTree left, BinarySearchTree right) {
        this.value = value;
        this.left = left;
        this.right = right;
    }

    @Override
    public BinarySearchTree insert(String v) {
        int cmp = v.compareTo(value);
        if (cmp == 0) {
            return this;
        } else if (cmp < 0) {
            return new Node(value, left.insert(v), right);
        } else {
            return new Node(value, left, right.insert(v));
        }
    }

    @Override
    public boolean contains(String v) {
        int cmp = v.compareTo(value);
        if (cmp == 0) return true;
        if (cmp < 0) return left.contains(v);
        return right.contains(v);
    }

    @Override
    public int size() {
        return 1 + left.size() + right.size();
    }


    @Override
    public String toString() {
        String leftStr = (left instanceof Empty) ? "" : left.toString();
        String rightStr = (right instanceof Empty) ? "" : right.toString();
        return "(" + leftStr + value + rightStr + ")";
    }
}
