from __future__ import print_function
from gensim.models.word2vec import Word2Vec
from gensim.models import KeyedVectors
import pickle as pkl
import argparse 
import os
from random_vec import RandomVec


class WordVec:
	"""docstring for WordVec"""
	def __init__(self, args):
		if args.restore is None:
			print('training')
			corpus = open(args.corpus,'r').read().lower().split() # space splited
			sentences = []
			sentence = []
			length = 0

			for word in corpus:
				sentence.append(word)
				length += 1
				if length == 20:
				 	sentences.append(sentence)
				 	sentence = []
				 	length = 0
			if length != 0:
				sentences.append(sentence)
			self.word_vec_model = Word2Vec(sentences = sentences, workers = 2, min_count = 1, sg = 1, window = 5, size = args.dimension)
			self.word_vec_model.save('train_model.bin')
		else :
			self.word_vec_model = Word2Vec.load(args.restore)
		self.random_model = RandomVec(args.dimension)

	def __getitem__(self, word):
		word = word.lower()
		try:
			return self.word_vec_model[word]
		except KeyError:
			return self.random_model[word]

parser = argparse.ArgumentParser()
parser.add_argument("--corpus", type = str, help='corpus location', default = "./feature_syn.txt")
parser.add_argument("--restore", type = str, default = "./train_model.bin", help = "Word2Vec place to save model")
parser.add_argument("--dimension", type=int, default=256, help = "Get dimension")
args = parser.parse_args()
model = WordVec(args)

# test_word = ["can","uv","instal","inlin","Pondmast","Fountain","Filter","kit"]

# for t_word in test_word:
# 	top10Words = model.word_vec_model.similar_by_vector(model[t_word], topn=2)
# 	for word,_ in top10Words:
# 		print(t_word, word)



# print(model.word_vec_model.most_similar(model["good"], topn=1))