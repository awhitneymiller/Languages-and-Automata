module Exercises
  ( firstThenApply
  , powers
  , countValidLines
  , Shape(..), surfaceArea, volume
  , BST(Empty), emptyBST, insertBST, memberBST, countBST, inorderBST
  ) where

import Data.List (find, intercalate)
import Data.Char (isSpace)

firstThenApply :: [a] -> (a -> Bool) -> (a -> b) -> Maybe b
firstThenApply xs p f = f <$> find p xs

powers :: Integral a => a -> [a]
powers b = map (b ^) [0..]

countValidLines :: FilePath -> IO Int
countValidLines path = do
  content <- readFile path
  let ls :: [String]
      ls = lines content
      valid :: String -> Bool
      valid line =
        let t = dropWhile isSpace line
        in not (null t) && head t /= '#'
  return (length (filter valid ls))

data Shape
  = Box { width :: Double, height :: Double, depth :: Double }
  | Sphere { radius :: Double }
  deriving (Eq, Show)

surfaceArea :: Shape -> Double
surfaceArea (Box w h d) = 2 * (w*h + w*d + h*d)
surfaceArea (Sphere r)  = 4 * pi * r^2

volume :: Shape -> Double
volume (Box w h d) = w * h * d
volume (Sphere r)  = (4/3) * pi * r^3

data BST a = Empty | Node (BST a) a (BST a)

emptyBST :: BST a
emptyBST = Empty

insertBST :: Ord a => a -> BST a -> BST a
insertBST x Empty = Node Empty x Empty
insertBST x (Node l v r)
  | x == v    = Node l v r
  | x <  v    = Node (insertBST x l) v r
  | otherwise = Node l v (insertBST x r)

memberBST :: Ord a => a -> BST a -> Bool
memberBST _ Empty        = False
memberBST x (Node l v r)
  | x == v    = True
  | x <  v    = memberBST x l
  | otherwise = memberBST x r

countBST :: BST a -> Int
countBST Empty        = 0
countBST (Node l _ r) = 1 + countBST l + countBST r

inorderBST :: BST a -> [a]
inorderBST Empty        = []
inorderBST (Node l v r) = inorderBST l ++ [v] ++ inorderBST r

instance Show a => Show (BST a) where
  show t = "BST(" ++ intercalate "," (map show (inorderBST t)) ++ ")"
