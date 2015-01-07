namespace java CoreNLP
namespace py corenlp

struct ParseTree 
{
	1:string tree,
	2:double score
}

struct NamedEntity
{
	1:string entity,
	2:string tag,
	3:i32 startOffset,
	4:i32 endOffset,
	5:i32 sentence_number
}

struct TaggedToken
{
	1:string tag,
	2:string token
}

exception SerializedException
{
    1: required binary payload
}

service StanfordCoreNLP
{
    void ping(),
    oneway void zip(),
    list<ParseTree> parse_text(1:string text, 2:list<string> outputFormat),
    ParseTree parse_tokens(1:list<string> tokens, 2:list<string> outputFormat),
    ParseTree parse_tagged_sentence(1:string taggedSentence, 2:list<string> outputFormat, 3:string divider),
    // remove as it requires Java 8
    //string lexicalize_parse_tree(1:string tree),
    list<NamedEntity> get_entities_from_text(1:string text),
    list<NamedEntity> get_entities_from_tokens(1:list<string> tokens),
    list<NamedEntity> get_entities_from_trees(1:list<string> trees),
    list<string> resolve_coreferences_in_text(1:string text),
    list<string> resolve_coreferences_in_tokenized_sentences(1:list<string> sentencesWithTokensSeparatedBySpace),
    list<string> resolve_coreferences_in_trees(1:list<string> trees),
    list<string> evaluate_tregex_pattern(1:string parseTree, 2:string tregexPattern),
    // pos tagging methods
    list<list<TaggedToken>> tag_text(1:string untokenizedText),
    list<TaggedToken> tag_tokenized_sentence(1:list<string> tokenizedSentence),
    list<TaggedToken> tag_partially_tagged_tokenized_sentence(1:string ptaggedtokenizedSentence, 2:string divider),
    // end of pos tagging methods
    string untokenize_sentence(1:list<string> sentenceTokens),
    list<list<string>> tokenize_text(1:string arbitraryText),
    ParseTree sr_parse_tagged_sentence(1:string taggedSentence, 2:list<string> outputFormat, 3:string divider),
    list<ParseTree> sr_parse_text(1:string untokenizedText, 2:list<string> outputFormat),
    ParseTree sr_parse_tokens(1:list<string> tokenizedSentence, 2:list<string> outputFormat)
}
