from __future__ import print_function
import tensorflow as tf
import argparse 
import pickle

class LstmModel:
	"""docstring for LstmModel"""
	def __init__(self, arg):
		self.arg = arg
		self.input_data = tf.placeholder(tf.float32, [None, args.sentence_length, args.word_dim])
		self.output_data = tf.placeholder(tf.float32, [None, args.sentence_length, args.word_dim])
		# create two cell one for forward and one for backward
		fw_cell = tf.nn.rnn_cell.LSTMCell(args.rnn_size, state_is_tuple = True)
		fw_cell = tf.nn.rnn_cell.DropoutWrapper(fw_cell, output_keep_prob = 0.5) # 0.5 prediction then send
		fw_cell = tf.nn.rnn_cell.MultiRNNCell([fw_cell] * args.nums_layers, state_is_tuple = True)

		#backward cell
		bw_cell = tf.nn.rnn_cell.LSTMCell(args.rnn_size, state_is_tuple = True)
		bw_cell = tf.nn.rnn_cell.DropoutWrapper(bw_cell, output_keep_prob = 0.5)
		bw_cell = tf.nn.rnn_cell.MultiRNNCell([bw_cell] * args.nums_layers, state_is_tuple = True)

		# create lstm
		# get length 
		self.length = tf.cast(tf.reduce_max(tf.abs(self.input_data), axis=2), tf.int32)
		output, _, _ = tf.nn.bidirectional_dynamic_rnn(fw_cell, bw_cell,
								tf.unpack(tf.transpose(self.input_data, perm = [1,0,2])),
								dtype = tf.float32, sequence_length=self.length)
		weight, bias = self.weight_and_bias(2 * args.rnn_size, args.class_size)
		output = tf.reshape(tf.transpose(tf.pack(output), perm=[1, 0, 2]), [-1, 2 * args.rnn_size]) # re transpose
		prediction = tf.nn.soft_max(tf.matmul(output, weight)+bias)
		self.prediction = tf.reshape(prediction, [-1, args.sentence_length, args.class_size])
		optimizer = tf.train.AdamOptimizer(0.0005)
		tvars = tf.trainable_variables()
		self.loss = self.cost_function()
		grads,_ = tf.clip_by_global_norm(tf.gradients(self.loss, tvars), 10)
		self.train_var = optimizer.apply_gradients(zip(grads, tvars))

	def cost_function(self):
		entropy = -tf.reduce_sum(self.output_data * tf.log(self.prediction))
		avg_max = tf.sign(tf.reduce_max(tf.abs(self.output_data), axis=2))
		entropy *= avg_max
		entropy = tf.reduce_sum(entropy, axis=1)
		entropy /= tf.cast(self.length, tf.float32)
		return tf.reduce_mean(entropy)

	@staticmethod
    def weight_and_bias(in_size, out_size):
        weight = tf.truncated_normal([in_size, out_size], stddev=0.01)
        bias = tf.constant(0.1, shape=[out_size])
		return tf.Variable(weight), tf.Variable(bias)


	def get_train_data():
		data = pickle.load(open('word_vec_model.pkl', 'rb'))
		print('test data loaded')
		return data 


parser = argparse.ArgumentParser()
parser.add_argument("--word_dim", type=int, help = "word vector dimensions", required=True)
parser.add_argument("--rnn_size", type=int, default=256, help="dimensions of rnn")
parser.add_argument("--sentence_length", type=int, help="max sentences length", required=True)
args = parser.parse_args()
model = LstmModel(args)