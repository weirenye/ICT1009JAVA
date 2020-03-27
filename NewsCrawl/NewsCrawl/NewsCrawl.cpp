#define CURL_STATICLIB

#ifdef _CRT_SECURE_NO_WARNINGS
#undef _CRT_SECURE_NO_WARNINGS
#endif
#define _CRT_SECURE_NO_WARNINGS 1

#include <iostream>
#include <string>
//#include "stdafx.h"
#include <stdio.h>
#include "curl/curl.h"
#include <fstream>
#include <sstream>
#include <regex> 
#include <iomanip>

#ifdef _DEBUG
#pragma comment (lib, "curl/libcurl_a_debug.lib")
#else
#pragma comment (lib, "curl/libcurl_a.lib")
#endif
#pragma comment (lib, "Normaliz.lib")
#pragma comment (lib, "Ws2_32.lib")
#pragma comment (lib, "Wldap32.lib")
#pragma comment (lib, "advapi32.lib")
#pragma warning(disable : 4996)

using namespace std;

int convert(char* argv1, char* argv2);
vector<string> csvColumn;


static size_t my_write(void* buffer, size_t size, size_t nmemb, void* param)
{
    std::string& text = *static_cast<std::string*>(param);
    size_t totalsize = size * nmemb;
    text.append(static_cast<char*>(buffer), totalsize);
    return totalsize;
}

void findreplace(string& data, string toSearch, string replaceStr)
{
    // Get the first occurrence
    size_t pos = data.find(toSearch);

    // Repeat till end is reached
    while (pos != std::string::npos)
    {
        // Replace this occurrence of Sub String
        data.replace(pos, toSearch.size(), replaceStr);
        // Get the next occurrence from the current position
        pos = data.find(toSearch, pos + replaceStr.size());
    }


}

class crawl {
public:
    string apiKey;
    string result;
    void search(string);
    //void findreplace(string&, string, string);
    void setapiKey(string);
    void cleanJSON();
    vector<string> split(const string& str, const string& delim);
    void eraseSubStr(string&, const string&);
};

void crawl::setapiKey(string key) {
    apiKey = key;
}

void crawl::search(string searchTerm) {
    // defining the filenames

    findreplace(searchTerm, " ", "_");
    string jsonname = searchTerm + ".json";
    string csvname = searchTerm + ".csv";

    //original link = "https://newsapi.org/v2/everything?q=global+warming&from=2020-03-26&sortBy=popularity&apiKey=cf3c29fb2b5c40d0854f25e802a04508";

    // setting up the link where we will extract the JSON string from
    findreplace(searchTerm, "_", "%20");
    string link = "https://newsapi.org/v2/everything?q=" + searchTerm + "&sortBy=popularity&apiKey=" + apiKey + "&pageSize=100";

    // using CURL to extract the JSON file generated by the API

    ofstream jsonDoc(jsonname);
    CURL* curl;
    CURLcode res;
    curl_global_init(CURL_GLOBAL_DEFAULT);
    curl = curl_easy_init();
    if (curl) {
        curl_easy_setopt(curl, CURLOPT_URL, link);
        curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, my_write);
        curl_easy_setopt(curl, CURLOPT_WRITEDATA, &result);
        curl_easy_setopt(curl, CURLOPT_VERBOSE, 1L);
        res = curl_easy_perform(curl);
        curl_easy_cleanup(curl);
        if (CURLE_OK != res) {
            std::cerr << "CURL error: " << res << '\n';
        }
    }
    curl_global_cleanup();

    // modifying the json string as the API has its own json format
    cleanJSON();
    // view the results in the console
    cout << result;
    // insert sting into the json file
    jsonDoc << result;
    jsonDoc.close();

    //std::cout << result << "\n\n";

    char* j = const_cast<char*>(jsonname.c_str());
    char* c = const_cast<char*>(csvname.c_str());

    // convert the JSON file into csv
    convert(j, c);

}

void crawl::eraseSubStr(std::string& mainStr, const std::string& toErase)
{
    // Search for the substring in string
    size_t pos = mainStr.find(toErase);

    if (pos != std::string::npos)
    {
        // If found then erase it from string
        mainStr.erase(pos, toErase.length());
    }
}

void crawl::cleanJSON() {

    // find the substring to remove
    string s2(result.begin(), std::find(result.begin(), result.end(), '['));

    // removing the substring from json file
    eraseSubStr(result, s2);
    result = result.substr(0, result.size() - 1);
}

vector<string> crawl::split(const string& str, const string& delim)
{
    vector<string> tokens;
    size_t prev = 0, pos = 0;
    do
    {
        pos = str.find(delim, prev);
        pos--;
        if (pos == string::npos) {
            pos = str.length();
        }
        string token = str.substr(prev, pos - prev);
        if (!token.empty()) {
            tokens.push_back(token);
            break;
        }
        prev = pos + delim.length();
    } while (pos < str.length() && prev < str.length());
    return tokens;
}

class article {
public:
    string author;
    string content;
    string description;
    string publishedAt;
    string id;
    string name;
    string title;
    string url;
    string urltoImage;
    // set all the values of the object
    void setAuthor(string);
    void setContent(string);
    void setDescription(string);
    void setPublishedAt(string);
    void setID(string);
    void setName(string);
    void setTitle(string);
    void setURL(string);
    void setImage(string);
    // get all the values of the object
    string getAuthor();
    string getContent();
    string getDescription();
    string getPublishedAt();
    string getID();
    string getName();
    string getTitle();
    string getURL();
    string getImage();
    // reset the values inside the object
    void resetti();
    // print the details:
    void printDetails();
};

// set all the variables in article

void article::setAuthor(string author) {
    this->author = author;
}

void article::setContent(string content) {
    this->content = content;
}

void article::setDescription(string description) {
    this->description = description;
}

void article::setPublishedAt(string publishedAt) {
    this->publishedAt = publishedAt;
}

void article::setID(string id) {
    this->id = id;
}

void article::setName(string name) {
    this->name = name;
}

void article::setTitle(string title) {
    this->title = title;
}

void article::setURL(string url) {
    this->url = url;
}

void article::setImage(string urltoImage) {
    this->urltoImage = urltoImage;
}

// now to get the variables from the object

string article::getAuthor() {
    return author;
}

string article::getContent() {
    return content;
}

string article::getDescription() {
    return description;
}

string article::getPublishedAt() {
    return publishedAt;
}

string article::getID() {
    return id;
}

string article::getName() {
    return name;
}

string article::getTitle() {
    return title;
}

string article::getURL() {
    return url;
}

string article::getImage() {
    return urltoImage;
}

void article::resetti() {
    author = "";
    content = "";
    description = "";
    publishedAt = "";
    id = "";
    name = "";
    title = "";
    url = "";
    urltoImage = "";
}

void article::printDetails() {
    cout << "author: " << author << endl;
    cout << "content: " << content << endl;
    cout << "description: " << description << endl;
    cout << "publishedAt: " << publishedAt << endl;
    cout << "id: " << id << endl;
    cout << "name: " << name << endl;
    cout << "title: " << title << endl;
    cout << "url: " << url << endl;
    cout << "urltoImage: " << urltoImage << endl;
}

vector<article> parseCSV(string csvname) {

   
    findreplace(csvname, " ", "_");
    std::ifstream f(csvname+".csv");
    cout << csvname << endl;
    //vector<string> acticles;

    string line;
    while (std::getline(f, line)) {
        //cout << line << endl;
        const char* mystart = line.c_str();
        bool instring{ false };
        for (const char* p = mystart; *p; p++) {
            if (*p == '"')
                instring = !instring;
            else if (*p == ',' && !instring) {
                csvColumn.push_back(string(mystart, p - mystart));
                mystart = p + 1;
            }
        }
        csvColumn.push_back(string(mystart));
       //cout << string(mystart) << endl;
    }

    f.close();

    // insert data into objects
    vector<article> acticles;
    //acticles.resize(100);
    article details;
    int ind = 0, max = 9;

    for (auto& x : csvColumn) {
        //cout << x << endl;
        
        switch (ind) {
            case 0:
                details.setAuthor(x);
            case 1:
                details.setContent(x);
            case 2:
                details.setDescription(x);
            case 3:
                details.setPublishedAt(x);
            case 4:
                details.setID(x);
            case 5:
                details.setName(x);
            case 6:
                details.setTitle(x);
            case 7:
                details.setURL(x);
            case 8:
                details.setImage(x);
        }
        if (ind == max) { 
            ind = 0;
            acticles.push_back(details);
            details.resetti();
        }
        ind++;
        
        
    }
    return acticles;
}

int main() {

    string searchTerm = "global warming";

    crawl topic;
    topic.setapiKey("cf3c29fb2b5c40d0854f25e802a04508");
    topic.search(searchTerm);

    vector<article> allArticles = parseCSV(searchTerm);

    // for demo purposes
    for (int i = 0; i < allArticles.size(); i++) {
        allArticles[i].printDetails();
    }
}












// To convert JSON files into CSV files
// credits to https://github.com/once-ler/json2csv-cpp/blob/master/index.cpp

// to install jsoncpp, a library used for this function.
//CMD commands:
//cd vcpkg
//vcpkg install jsoncpp

#include <iostream>
#include <fstream>
#include <algorithm>
#include <set>
#include <sstream>
#include <memory>
#include <regex>
#include <json/json.h>

using namespace std;

typedef shared_ptr<std::vector<shared_ptr<std::vector<std::pair<string, string>>>>> objects_t;
typedef shared_ptr<std::vector<std::pair<string, string>>> object_t;
typedef vector<unique_ptr<string>> line_t;
typedef vector<shared_ptr<vector<unique_ptr<string>>>> lines_t;

bool jsonToCsv(shared_ptr<Json::Value> jsonInput, const char* input, const char* output) {

    bool parsingSuccessful = true;
    Json::Reader  reader;
    ifstream ifs(input);

    if (ifs.is_open()) {
        istream& s = ifs;
        parsingSuccessful = reader.parse(s, *jsonInput);
        if (!parsingSuccessful) {
            cout << "Error parsing file => " << input << "\n";
        }
    }
    else {
        cout << "File not found => " << input << "\n";
        parsingSuccessful = false;
    }
    ifs.close();
    return parsingSuccessful;
}

string joinVector(vector<string>& v, const char* delimiter) {
    std::stringstream ss;
    for (size_t i = 0; i < v.size(); ++i)
    {
        if (i != 0)
            ss << delimiter;
        ss << v[i];
    }
    return ss.str();
}

void toKeyValuePairs(shared_ptr<std::vector<std::pair<string, string>>>& builder, Json::Value& source, vector<string>& ancestors, const char* delimiter) {

    if (source.isObject()) {
        for (Json::Value::iterator it = source.begin(); it != source.end(); it++) {
            Json::Value key = it.key();
            Json::Value val = (*it);
            vector<string> objKeys;
            objKeys.insert(objKeys.end(), ancestors.begin(), ancestors.end());
            objKeys.push_back(key.asString());
            toKeyValuePairs(builder, val, objKeys, "/");
        }
    }
    else if (source.isArray()) {
        int count = 0;
        std::for_each(source.begin(), source.end(), [&builder, &count, &ancestors](Json::Value& val) {
            stringstream ss;
            ss << count;
            vector<string> arrKeys;
            arrKeys.insert(arrKeys.end(), ancestors.begin(), ancestors.end());
            arrKeys.push_back(ss.str());
            toKeyValuePairs(builder, val, arrKeys, "/");
            count++;
            });
    }
    else {
        string key = joinVector(ancestors, delimiter);
        auto tpl = std::make_pair(key, source.asString());
        builder->push_back(tpl);
    }

}

objects_t jsonToDicts(shared_ptr<Json::Value> jsonInput) {

    //convert json into array if not already
    if (!jsonInput->isArray()) {
        Json::Value jv;
        jv.append(std::move(*jsonInput));
        *jsonInput = jv;
    }

    auto objects = make_shared<std::vector<shared_ptr<std::vector<std::pair<string, string>>>>>();

    std::for_each(jsonInput->begin(), jsonInput->end(), [&objects](Json::Value& d) {
        auto builder = make_shared<std::vector<std::pair<string, string>>>();
        objects->push_back(builder);
        auto o = objects->back();
        vector<string> keys;
        toKeyValuePairs(o, d, keys, "/");
        });

    return objects;
}

shared_ptr<lines_t> dictsToCsv(objects_t o) {

    std::set<string> k;
    auto lines = make_shared<lines_t>();
    std::regex newline("\\r|\\n");
    std::regex quote("\"");

    auto buildKeys = [&k](object_t& e) {
        for (auto& g : *e) {
            k.insert(g.first);
        }
    };

    auto buildHeaders = [&k, &lines]() {
        auto l = make_shared<line_t>();
        for (auto& h : k) {
            l.get()->push_back(make_unique<string>(h));
        }
        lines.get()->push_back(l);
    };

    auto buildRows = [&k, &lines, &newline, &quote](object_t& e) {
        auto l = make_shared<line_t>();
        for (auto& h : k) {
            auto it = std::find_if(e.get()->begin(), e.get()->end(), [&h](const std::pair<string, string>& p)->bool {
                return p.first == h;
                });

            if (it != e.get()->end()) {
                std::string n = std::regex_replace((it)->second, newline, "\\\\n");
                n = std::regex_replace(n, quote, "\"\"");
                l.get()->push_back(make_unique<string>("\"" + n + "\""));
                e.get()->erase(it);
            }
            else {
                l.get()->push_back(make_unique<string>());
            }
        }
        lines.get()->push_back(l);

    };

    std::for_each(o->begin(), o->end(), buildKeys);
    buildHeaders();
    std::for_each(o->begin(), o->end(), buildRows);

    return lines;
}

//int convert(int argc, char* argv[]) {
int convert(char* argv1, char* argv2) {
    cout << endl << "HELLO WORLD";

    auto jsonInput = make_shared<Json::Value>();

    //check if input file exists
    auto ok = jsonToCsv(jsonInput, argv1, argv2);
    if (!ok) {
        exit(1);
    }

    //flatten the objects
    auto objects = jsonToDicts(jsonInput);

    //merge and sort the keys
    auto csv = dictsToCsv(objects);

    //export file
    ofstream ofs;
    ofs.open(argv2);
    for (const auto& e : *csv) {
        auto len = e.get()->size();
        int counter = 0;
        for (const auto& g : *e) {
            ofs << *g << (counter < len - 1 ? "," : "");
            counter++;
        }
        ofs << "\n";
    }
    ofs.close();

    return 0;
}