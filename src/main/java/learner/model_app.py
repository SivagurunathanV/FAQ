from flask import Flask
from flask_restful import Resource, Api, reqparse
from test_learner import WordVec
from flask import request
from flask import make_response
from flask import jsonify 
import json
from json import JSONEncoder
import argparse 

app = Flask(__name__)
api = Api(app)

class WordVecModel(Resource,JSONEncoder):
	def default(self, o):
            return o.__dict__   
	def post(self):
		# args = request.args
		# words = args['words']
		# args = request.args
		# words = args['words']	
		data = request.json
		# output 	= {
		# 	'words' : data['words']
		# }
		data_list = data['words'].split(",")
		parser = argparse.ArgumentParser()
		parser.add_argument("--corpus", type = str, help='corpus location', default = "./feature_syn.txt")
		parser.add_argument("--restore", type = str, default = "./train_model.bin", help = "Word2Vec place to save model")
		parser.add_argument("--dimension", type=int, default=256, help = "Get dimension")
		args = parser.parse_args()
		self.model = WordVec(args)
		word_mapping = list()
		for t_word in data_list:
			result = list()
			top10Words = self.model.word_vec_model.similar_by_vector(self.model[t_word], topn=2)
			for word,_ in top10Words:
				result.append(word)
			word_mapping.append(result)
		output 	= {
			'words' : word_mapping
		}
		return output
		# return make_response(json.dumps(output), 200)

api.add_resource(WordVecModel, '/most_similar')

if __name__ == '__main__':
	app.run(debug = True)
	