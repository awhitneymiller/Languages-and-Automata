import Foundation

func firstThenApply<A, B>(
    _ arr: [A],
    predicate: (A) -> Bool,
    transform: (A) -> B
) -> B? {
    return arr.first(where: { x in predicate(x) }).map { x in
        transform(x)
    }
}

// exercise 1
func firstThenLowerCase(
    of arr: [String],
    satisfying predicate: (String) -> Bool
) -> String? {
    return arr.first(where: { s in predicate(s) })?.lowercased()
}

// exercise 2
struct PhraseChain {
    let phrase: String

    func and(_ next: String) -> PhraseChain {
        PhraseChain(phrase: "\(phrase) \(next)")
    }
}

func say() -> PhraseChain {
    PhraseChain(phrase: "")
}

func say(_ first: String) -> PhraseChain {
    PhraseChain(phrase: first)
}

// exercise 3
func meaningfulLineCount(_ filename: String) async -> Result<Int, Error> {
    do {
        let contents = try String(contentsOfFile: filename, encoding: .utf8)
        let lines = contents.split(separator: "\n", omittingEmptySubsequences: false)
        let count = lines.filter { rawLine in
            let trimmed = rawLine.trimmingCharacters(in: .whitespacesAndNewlines)
            return !(trimmed.isEmpty || trimmed.hasPrefix("#"))
        }.count
        return .success(count)
    } catch {
        return .failure(error)
    }
}

// exercise 4
struct Quaternion: Equatable, CustomStringConvertible {
    let a: Double
    let b: Double
    let c: Double
    let d: Double

    init(a: Double = 0.0, b: Double = 0.0, c: Double = 0.0, d: Double = 0.0) {
        self.a = a
        self.b = b
        self.c = c
        self.d = d
    }

    static let ZERO = Quaternion(a: 0.0, b: 0.0, c: 0.0, d: 0.0)
    static let I = Quaternion(a: 0.0, b: 1.0, c: 0.0, d: 0.0)
    static let J = Quaternion(a: 0.0, b: 0.0, c: 1.0, d: 0.0)
    static let K = Quaternion(a: 0.0, b: 0.0, c: 0.0, d: 1.0)

    var coefficients: [Double] {
        [a, b, c, d]
    }

    var conjugate: Quaternion {
        Quaternion(a: a, b: -b, c: -c, d: -d)
    }

    static func + (lhs: Quaternion, rhs: Quaternion) -> Quaternion {
        Quaternion(
            a: lhs.a + rhs.a,
            b: lhs.b + rhs.b,
            c: lhs.c + rhs.c,
            d: lhs.d + rhs.d
        )
    }

    static func * (lhs: Quaternion, rhs: Quaternion) -> Quaternion {
        let A = lhs.a, B = lhs.b, C = lhs.c, D = lhs.d
        let E = rhs.a, F = rhs.b, G = rhs.c, H = rhs.d
        return Quaternion(
            a: A * E - B * F - C * G - D * H,
            b: A * F + B * E + C * H - D * G,
            c: A * G - B * H + C * E + D * F,
            d: A * H + B * G - C * F + D * E
        )
    }

    private func appendComponent(
        _ sb: inout String,
        coef: Double,
        symbol: String,
        isFirstTerm: Bool
    ) {
        if coef == 0.0 { return }

        let neg = coef < 0
        let absVal = abs(coef)

        if isFirstTerm {
            if neg { sb += "-" }
        } else {
            if neg { sb += "-" } else { sb += "+" }
        }

        let isUnit = (absVal == 1.0)
        if !isUnit {
            sb += String(absVal)
        }

        sb += symbol
    }

    var description: String {
        var s = ""
        if a != 0.0 {
            s += String(a)
        }

        appendComponent(&s, coef: b, symbol: "i", isFirstTerm: s.isEmpty)
        appendComponent(&s, coef: c, symbol: "j", isFirstTerm: s.isEmpty)
        appendComponent(&s, coef: d, symbol: "k", isFirstTerm: s.isEmpty)

        if s.isEmpty {
            return "0"
        }
        return s
    }
}

// exercise 5
indirect enum BinarySearchTree: CustomStringConvertible {
    case empty
    case node(String, BinarySearchTree, BinarySearchTree)

    func insert(_ value: String) -> BinarySearchTree {
        switch self {
        case .empty:
            return .node(value, .empty, .empty)
        case let .node(v, left, right):
            if value == v {
                return self
            } else if value < v {
                return .node(v, left.insert(value), right)
            } else {
                return .node(v, left, right.insert(value))
            }
        }
    }

    func contains(_ value: String) -> Bool {
        switch self {
        case .empty:
            return false
        case let .node(v, left, right):
            if value == v { return true }
            if value < v { return left.contains(value) }
            return right.contains(value)
        }
    }

    var size: Int {
        switch self {
        case .empty:
            return 0
        case let .node(_, left, right):
            return 1 + left.size + right.size
        }
    }

    private func internalString(root: Bool) -> String {
        switch self {
        case .empty:
            return root ? "()" : ""
        case let .node(v, left, right):
            let l = left.internalString(root: false)
            let r = right.internalString(root: false)
            return "(\(l)\(v)\(r))"
        }
    }

    var description: String {
        internalString(root: true)
    }
}
