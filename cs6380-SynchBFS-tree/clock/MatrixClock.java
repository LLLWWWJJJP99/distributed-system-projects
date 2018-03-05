package cs6380.clock;

public class MatrixClock {
	private int[][] matrix;
	private int n;
	private int d;
	public MatrixClock(int n) {
		this.matrix = new int[n][n];
		this.n = n;
		this.d = 1;
	}

	public int[][] getMatrix() {
		return matrix;
	}

	/**
	 * true if compared message could be delivered, otherwise false
	 * @param message_matrix matrix clock of another message to be compared
	 * @param sender sender of compared message
	 * @param receiver of compared message
	 * @return whether compared message could be delivered
	 */
	public synchronized boolean canBeDelivered(int[][] message_matrix, int sender, int receiver) {
		for(int i = 0; i < n; i++) {
			if(i != sender && message_matrix[i][receiver] > matrix[i][receiver]) {
				return false;
			}
		}
		return message_matrix[sender][receiver] == matrix[sender][receiver] + 1;
	}
	
	/**update current matrix clock according to the given message
	 * @param message_matrix
	 */
	public synchronized void event_clock(int[][] message_matrix) {
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				matrix[i][j] = Math.max(matrix[i][j], message_matrix[i][j]);
			}
		}
	}
	
	/** increase sender's knowledge of number of messages receiver has sent by one
	 * @param sender
	 * @param receviver
	 */
	public synchronized void local_increase(int sender, int receviver) {
		matrix[sender][receviver] += d;
	}

}
