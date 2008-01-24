package org.gdms.data.indexes.btree;

import org.gdms.data.values.Value;
import org.gdms.data.values.ValueFactory;

public class BTreeLeaf extends AbstractBTreeNode implements BTreeNode {

	private int[] rows;
	private BTreeLeaf rightNeighbour;

	public BTreeLeaf(BTreeInteriorNode parent, int n) {
		super(parent, n);
		rows = new int[n + 1];
	}

	public BTreeLeaf getChildNodeFor(Value v) {
		return this;
	}

	public BTreeNode insert(Value v, int rowIndex) {
		if (valueCount == n) {
			// insert the value, split the node and reorganize the tree
			valueCount = insertValue(v, rowIndex, values, valueCount, rows);
			BTreeLeaf right = new BTreeLeaf(null, n);
			for (int i = (n + 1) / 2; i <= n; i++) {
				right.insert(values[i], rows[i]);
				values[i] = null;
				rows[i] = 0;
			}
			valueCount = (n + 1) / 2;
			this.rightNeighbour = right;

			if (parent == null) {
				// It's the root
				BTreeInteriorNode newRoot = new BTreeInteriorNode(null, n,
						this, right);

				return newRoot;
			} else {
				return parent.reorganize(right.getSmallestValueNotIn(this),
						right);
			}
		} else {
			valueCount = insertValue(v, rowIndex, values, valueCount, rows);
			return null;
		}
	}

	private int insertValue(Value v, int rowIndex, Value[] values,
			int valueCount, int[] rows) {
		// Look the place to insert the new value
		int index = valueCount;
		for (int i = 0; i < valueCount; i++) {
			if (v.less(values[i]).getAsBoolean()) {
				// will insert at i
				index = i;
				break;
			}
		}

		// insert in index
		for (int j = valueCount; j >= index + 1; j--) {
			values[j] = values[j - 1];
			rows[j] = rows[j - 1];
		}
		values[index] = v;
		rows[index] = rowIndex;
		valueCount++;

		return valueCount;
	}

	public boolean isLeave() {
		return true;
	}

	public int[] getIndex(Value value) {
		int[] thisRows = new int[n];
		int index = 0;
		for (int i = 0; i < valueCount; i++) {
			if (values[i].equals(value).getAsBoolean()) {
				thisRows[index] = rows[i];
				index++;
			}
		}

		if (index < valueCount) {
			int[] ret = new int[index];
			System.arraycopy(thisRows, 0, ret, 0, index);
			return ret;
		} else {
			int[] moreRows = null;
			if (rightNeighbour != null) {
				moreRows = rightNeighbour.getIndex(value);
			} else {
				moreRows = new int[0];
			}

			int[] ret = new int[index + moreRows.length];
			System.arraycopy(thisRows, 0, ret, 0, index);
			System.arraycopy(moreRows, 0, ret, index, moreRows.length);
			return ret;
		}
	}

	@Override
	public String toString() {
		StringBuilder strValues = new StringBuilder("");
		String separator = "";
		for (Value v : this.values) {
			strValues.append(separator).append(v);
			separator = ", ";
		}

		return name + " (" + strValues.toString() + ") ";
	}

	public Value getSmallestValueNotIn(BTreeNode treeNode) {
		for (int i = 0; i < valueCount; i++) {
			if (!treeNode.contains(values[i])) {
				return values[i];
			}
		}

		return ValueFactory.createNullValue();

	}

	public boolean contains(Value v) {
		for (int i = 0; i < valueCount; i++) {
			if (values[i].equals(v).getAsBoolean()) {
				return true;
			}
		}
		return false;
	}

	public Value[] getAllValues() {
		Value[] thisRows = values;

		Value[] moreRows = null;
		if (rightNeighbour != null) {
			moreRows = rightNeighbour.getAllValues();
		} else {
			moreRows = new Value[0];
		}

		Value[] ret = new Value[valueCount + moreRows.length];
		System.arraycopy(thisRows, 0, ret, 0, valueCount);
		System.arraycopy(moreRows, 0, ret, valueCount, moreRows.length);
		return ret;

	}

	public BTreeLeaf getFirstLeaf() {
		return this;
	}
}