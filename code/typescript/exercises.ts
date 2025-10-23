import { createReadStream } from "fs";
import * as readline from "readline";

export function firstThenApply<A, B>(
  a: A[],
  p: (x: A) => boolean,
  f: (x: A) => B
): B | undefined {
  const hit = a.find(p);
  return hit === undefined ? undefined : f(hit);
}
export function* powers(base: bigint): Generator<bigint> {
  let n = 0n;
  while (true) {
    yield base ** n;
    n++;
  }
}

export async function countValidLines(path: string): Promise<number> {
  const fileStream = createReadStream(path, { encoding: "utf8" });
  const rl = readline.createInterface({
    input: fileStream,
    crlfDelay: Infinity,
  });

  let count = 0;
  for await (const line of rl) {
    const trimmed = line.trim();
    if (trimmed.length > 0 && !trimmed.startsWith("#")) {
      count++;
    }
  }
  return count;
}

export type Box = { kind: "box"; width: number; height: number; depth: number };
export type Sphere = { kind: "sphere"; radius: number };
export type Shape = Box | Sphere;

export function surfaceArea(s: Shape): number {
  switch (s.kind) {
    case "box":
      return 2 * (s.width * s.height + s.width * s.depth + s.height * s.depth);
    case "sphere":
      return 4 * Math.PI * s.radius ** 2;
  }
}

export function volume(s: Shape): number {
  switch (s.kind) {
    case "box":
      return s.width * s.height * s.depth;
    case "sphere":
      return (4 / 3) * Math.PI * s.radius ** 3;
  }
}

export function equals(a: Shape, b: Shape): boolean {
  if (a.kind !== b.kind) return false;
  return a.kind === "box"
    ? a.width === b.width && a.height === b.height && a.depth === b.depth
    : a.radius === b.radius;
}

export function toStringShape(s: Shape): string {
  return s.kind === "box"
    ? `Box(${s.width},${s.height},${s.depth})`
    : `Sphere(${s.radius})`;
}

type Cmp<A> = (a: A, b: A) => number;

interface Node<A> { readonly v: A; readonly l: Node<A> | null; readonly r: Node<A> | null; }

class BSTree<A> {
  private constructor(private readonly cmp: Cmp<A>, private readonly root: Node<A> | null) {}
  static empty<A>(cmp: Cmp<A>): BSTree<A> { return new BSTree(cmp, null); }

  insert(x: A): BSTree<A> {
    const ins = (n: Node<A> | null): Node<A> =>
      n === null
        ? { v: x, l: null, r: null }
        : this.cmp(x, n.v) === 0
          ? n
          : this.cmp(x, n.v) < 0
            ? { v: n.v, l: ins(n.l), r: n.r }
            : { v: n.v, l: n.l, r: ins(n.r) };
    return new BSTree(this.cmp, ins(this.root));
  }

  has(x: A): boolean {
    let n = this.root;
    while (n) {
      const c = this.cmp(x, n.v);
      if (c === 0) return true;
      n = c < 0 ? n.l : n.r;
    }
    return false;
  }

  count(): number {
    const f = (n: Node<A> | null): number => (n ? 1 + f(n.l) + f(n.r) : 0);
    return f(this.root);
  }

  *inorder(): Generator<A> {
    function* go<B>(n: Node<B> | null): Generator<B> {
      if (!n) return;
      yield* go(n.l);
      yield n.v;
      yield* go(n.r);
    }
    yield* go(this.root);
  }

  toString(): string {
    return `BST(${Array.from(this.inorder()).join(",")})`;
  }
}

export type BST<A> = BSTree<A>;
export const emptyBST = BSTree.empty;
export const insertBST = <A>(t: BST<A>, x: A) => t.insert(x);
export const lookupBST = <A>(t: BST<A>, x: A) => t.has(x);
export const countBST = <A>(t: BST<A>) => t.count();
export function* inorderBST<A>(t: BST<A>): Generator<A> { yield* t.inorder(); }
export const showBST = <A>(t: BST<A>) => t.toString();
