# FREQUENTLY ASKED QUESTIONS - NLP

### SIVAGURUNATHAN VELAYUTHAM
### SXV176330@UTDALLAS.EDU
### INTRODUCTION

Frequently asked questions popularly called as F.A.Q, provides a list of Questions and Answers, commonly asked in some context, and pertaining to a particular topic. FAQ are mostly used where questions tend to occur. The convenient way to share FAQ with others is writing an article and storing it in offline. In this case, the articles might not be FAQ - not necessarily questions and answers. However, FAQ used refer all those documents and postings which are offline. 
With advancement in Internet, people tend to share the documents or articles in online. People prefer to ask questions in online forums, chat with customer support and reading reviews. These modes helped the user to find the right answer which are relevant for them. In recent times, users having access to a lot of data, where they could not find an appropriate answer for the question. This leads to FAQ as irrelevant if the answer provided in one or more FAQ, user does not get the answer what he/she looks for. 
Natural Language processing (NLP) is a branch of artificial intelligence concerned with automatic interpretation and generation of human language like text, voice etc. It solves the problem of finding relevant question for user by applying NLP techniques like stemming, lemmatization and semantic features on the questions.

### REQUIREMENTS

Implement a FAQ that will produce improved results using NLP features and techniques.
Input will be a set of FAQ's and answers. User's input natural language question/statement and generate one or more FAQ's that match the user's input question/statement.




### DATASET:

This dataset contains Question and Answer data from Amazon by matching ASINs(Amazon Standard Identification Number). 

Sample Question and Answer:
 
 ![](https://github.com/SivagurunathanV/FAQ/blob/6f5ddd6bf653368dce43be8fc436a51369924b37/docs/sample_dataset.png)

1.	asin- id of the product
2.	questionType - type of question, could be yes/no or open-ended
3.	answerType - type of answer, could be yes/no or '?' (if the polarity of the answer could not be predicted)
4.	AnswerTime- raw answer time stamp
5.	UnixTime: converted to unix timestamp
6.	Question - question as text
7.	Answer - answer as text
Here we went with Pet Supplies in the product category, since it contains more natural and distinguishes text to process than any other category.



### ARCHITECTURE

 ![alt arch](https://github.com/SivagurunathanV/FAQ/blob/master/docs/arch.png)

### IMPLEMENTATION

After getting the dataset, following are the steps involved in implementing the NLP Pipeline
1.	From the input dataset, parse the JSON data and store the raw data in database
2.	Extract the raw data from database, and parse that to the Tokenization using PBT and do unigram count probability and add weights to each of the question and store the result back to the database
3.	From the SEARCH_UI Page, user types the question. It will flow through this pipeline and find the unigram probability for the user typed question and match the best probable from the database by brute force i.e. looking at all the records from the database
4.	Above method is not efficient, as it is scanning the entire database and comparing it with every record and find the best one. 
5.	Build an advanced NLP pipeline that can extract features like lemma, stem, part of speech tag, dependency parse tree and synonyms (other meaning from WordNet) for the given sentence and store the results back to the database for building the model.
6.	Built a model using the Word2Vec by aggregating the features extracted from the previous step. Here WordNet feature is used for training the model. 
7.	When user type the question from the ADVANCE_SEARCH_UI Page, the sentence will go through the advance NLP pipeline and extract the feature out of it.
8.	Send this extracted feature to the model and predict the most similar words. 
9.	In the next step, we extract the questions based on the predicted words from the model and display it to the user
10.	Update the model, after completing the request from the user. In this way model can be trained a lot more and its accuracy can be improved.

## RESULTS

### TEST CASE USING ADVANCE NLP TECHNIQUE
 
 ![](https://github.com/SivagurunathanV/FAQ/blob/6f5ddd6bf653368dce43be8fc436a51369924b37/docs/result.png)

### FUTURE WORK

With recent advancement in the deep learning, we can implement architecture based on the Recurrent Neural Networks (RNN's) and Convolutional Neural Networks (CNN) or combination of both. 
Using genism, we implemented a word2vec and find the most similar word from that. Other options like we can build this from RNN combined with bi-LSTM using tensor flow which can give better performance and accuracy for larger datasets.
For improving the query performance, we can use push this data to elastic search or solar so that processing on partial text will be even faster.

### TECHNOLOGIES USED

* PROGRAMMING LANGUAGES: JAVA, PYTHON
* TOOLS USED: STANFORD NLP, DROPWIZARD, HIBERNATE, GOOGLE GUAVA, JACKSON, JWI, FLASK
* SERVER: JERSEY
* UI COMPONENTS: HTML, CSS, JAVASCRIPT

RESOURCES AND LINKS

1.	https://eng.uber.com/cota/
2.	http://mccormickml.com/2016/03/25/lsa-for-text-classification-tutorial/
3.	http://jmcauley.ucsd.edu/data/amazon/qa/ (DATASET)
