from __future__ import print_function
import os
import json

def format_data_from_file(input_file):
	f = open(input_file)
	lines = f.readlines()
	feature_list = list()
	SYNONYMS_LIST = list()
	HYPERNYMS_LIST = list()
	for line in lines:
		l = line.split("\n")
		feature_map = json.loads(l[0])
		for word, gloss in feature_map.iteritems():
			# print(word)
			for key, meaning in gloss.iteritems():
				if key == "SYNONYMS":
					# print(json.dumps(meaning))
					# SYNONYMS_LIST.append(word.encode("utf-8"))
					# SYNONYMS_LIST.append(key)
					SYNONYMS_LIST.append(word + " ")
					for m_word in meaning:
						SYNONYMS_LIST.append(m_word + " ")
				else:
					# print(json.dumps(meaning))

					for m_word in meaning:
						HYPERNYMS_LIST.append(m_word.encode("utf-8"))
		# print(json.dumps(l))
		# feature_list.append(l[3].replace())
	of = open('./feature_syn.txt','w')
	of.writelines(SYNONYMS_LIST)
	of.close()
	# print(SYNONYMS_LIST)
	# print(HYPERNYMS_LIST)


format_data_from_file("/Users/sivagurunathanvelayutham/Documents/FAQ/src/main/java/learner/train_data_set.txt")