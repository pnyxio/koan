package io.pnyx.koan.io

//expect interface Uri {
//    companion object {
//        operator fun invoke(uri: String): Uri
//    }
//}
//
//expect interface URLSearchParams: Iterable<Pair<String, String>> {
//    fun append(name: String , value: String)
//    fun delete(name: String)
//    fun get(name: String): String?
//    fun getAll(name: String): List<String>
//    fun has(name: String): Boolean
//    fun set(name: String, value: String)
//
//    fun sort()
//
//
//    //stringifier
//    override fun toString(): String
//
//    companion object {
//        operator fun invoke(uri: String = ""): URLSearchParams
//        //[Constructor(optional (sequence<sequence<String>> or record<String, String> or String) init = ""),
//    }
//
//}

/* @see https://url.spec.whatwg.org/
[Constructor(String url, optional String base),
Exposed=(Window,Worker),
LegacyWindowAlias=webkitURL]
interface URL {
    stringifier attribute String href;
    readonly attribute String origin;
    attribute String protocol;
    attribute String username;
    attribute String password;
    attribute String host;
    attribute String hostname;
    attribute String port;
    attribute String pathname;
    attribute String search;
    [SameObject] readonly attribute URLSearchParams searchParams;
    attribute String hash;

    String toJSON();
};


[Constructor(optional (sequence<sequence<String>> or record<String, String> or String) init = ""),
 Exposed=(Window,Worker)]
interface URLSearchParams {
  void append(String name, String value);
  void delete(String name);
  String? get(String name);
  sequence<String> getAll(String name);
  boolean has(String name);
  void set(String name, String value);

  void sort();

  iterable<String, String>;
  stringifier;
};

*/