How to Get Parse Trees from the Stanford Parser via this Apache Thrift Server
=============================================================================

## The ParseTree data strucure

The core return type here is a data structure called `ParseTree` which has two members:

* `tree`: A string representing your parse tree (or, quite optionally, parse treeS; keep reading).
* `score`: A double representing the score for that parse.
   
## What one can do with the `outputFormat` argument to the methods which take it

The purpose of the `outputFormat` argument is to allow one to supply arguments in the same style as one would via command-line call to the Stanford Parser. **The only command-line switches supported here are `-outputFormat` and `-outputFormatOptions`, but they are supported in full.**  By that I mean any valid argument to each of those options is also valid here.
You can also pass in `null`/`None` and that will return parse trees in this server's default format of `-outputFormat "oneline"`.
You can also supply multiple `-outputFormat` arguments, but note: you'll get back all of those parse trees, but altogether in the `tree` member of the returned `ParseTree` object, separated by two newlines (`\n\n`).
Thus, a call to a client object `client` that looks like:

```python
result = client.parse_tokens(["The", "cat", "sat", "on", "the", "mat", "."], 
                             ["-outputFormat", "typedDependencies,penn", "-outputFormatOptions", "basicDependencies"])
```

will have the following inside the `tree` member:

```Python
(ROOT
  (S
    (NP (DT The) (NN cat))
    (VP (VBD sat)
      (PP (IN on)
        (NP (DT the) (NN mat))))
    (. .)))

det(cat-2, The-1)
nsubj(sat-3, cat-2)
root(ROOT-0, sat-3)
prep(sat-3, on-4)
det(mat-6, the-5)
pobj(on-4, mat-6)
```

## How to get ParseTree objects using the standard (lexicalized) parser

In order to obtain `ParseTree` objects (a.k.a. parse trees and scores), you have three choices, depending on whether or not you'd like Stanford's tokenizer to do some of the work for you.  The arguments are supplied in both Python and Java terms for ease of understanding, but again, see the clients if you're confused.  Keep reading for more information on the `outputFormat` parameter to each of these methods.

* `parse_text(text, outputFormat)` where `text` is a Java `String`/Python `str` or `unicode`, `outputFormat` is a Java `List<String>`/Python list containing `str`/`unicode`.
  Returns: Java `List<ParseTree>`/Python list containing `ParseTree` objects.
  Given any untokenized, arbitrary text, use Stanford's sentence and word tokenizers to do that bit of the work.

* `parse_tokens(tokens, outputFormat)` where `tokens` is a Java `List<String>`/Python list containing `str`/`unicode`, `outputFormat` is a Java `List<String>`/Python list containing `str`/`unicode`.
   Returns: A `ParseTree` object.
   Given a single sentence worth of output from the sentence and word tokenizers of your choice, return that sentence's corresponding result from Stanford Parser.  Does not use Stanford's tokenizers.

* `parse_tagged_sentence(taggedSentence, outputFormat, delimiter)` where `taggedSentence` is a single POS-tagged sentence (Java `String`/Python `str`/`unicode`), `outputFormat` is a Java `List<String>`/Python list containing `str`/`unicode`, and `delimiter` is a Java `String`/Python `str`/`unicode` containing the single character that separates the word from the tag in your `taggedSentence`.
	Returns: A `ParseTree` object.
	Given a single Penn Treebank part-of-speech-tagged sentence from the tokenizer and tagger combination of your choice, have Stanford generate a parse tree based on those tags.

If you already have a `ParseTree` or a `String`/`str` or `unicode` that represents a valid parse tree, and you'd like to find the head words for each phrase, you can call `lexicalize_parse_tree(tree)` where `tree` a Java `String`/Python `str`/`unicode`.  This method returns a `String`/etc. that would be the same if you generated a parse tree using any of the methods above and had specified `-outputFormatOptions lexicalize` in the `outputFormat` argument.  Please note that if you pass in a tree that is already lexicalized, CoreNLP will just re-lexicalize that tree for you, resulting in duplicate head word information.  Whatever format `tree` was in when you called this function will be the same format as your output, only the tree itself will be annotated with head word information.

## How to get ParseTree objects using the shift-reduce parser

The methods for the shift-reduce parser are identical in function and behaviour to those described above for the standard parser, with the names `sr_parse_text()`, `sr_parse_tokens()`, and `sr_parse_tagged_sentence()`.  The real difference here is that the shift-reduce parser only works on pre-POS-tagged input, so if you use either of the first two methods, the Stanford Tagger will be run on your text/tokens.  Aside from that, the shift-reduce is crazy fast compared to the standard parser.

Additionally, every score I receive for a parse tree from the shift-reduce parser has been `nan`.  I'm looking into it.

Parse trees generated with the shift-reduce parser can be fed into the `lexicalize_parse_tree()` method, described above, just fine.
