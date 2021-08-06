public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        ArrayDeque<Character> d = new ArrayDeque<>();
        for (int i = 0; i < word.length(); i++) {
            d.addLast(word.charAt(i));
        }
        return d;
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> stringDeque = wordToDeque(word);
        return isPalindrome(stringDeque, cc);
    }

    private boolean isPalindrome(Deque<Character> word, CharacterComparator c) {
        if (word.size() == 0 || word.size() == 1) {
            return true;
        }
        Character last = word.removeLast();
        Character first = word.removeFirst();

        if (!c.equalChars(last, first)) {
            return false;
        }
        return isPalindrome(word, c);
    }


    public boolean isPalindrome(String word) {
        Deque<Character> stringDeque = wordToDeque(word);
        return isPalindrome(stringDeque);
    }

    private boolean isPalindrome(Deque<Character> word) {
        if (word.size() == 0 || word.size() == 1) {
            return true;
        }
        Character last = word.removeLast();
        Character first = word.removeFirst();

        if (!last.equals(first)) {
            return false;
        }
        return isPalindrome(word);
    }
}
