package ua.space.control;

public class KeyboardControl {
	private boolean rigth;
	private boolean left;
	private boolean forward;
	private boolean space;

	public boolean isRigth() {
		return rigth;
	}

	public boolean isLeft() {
		return left;
	}

	public boolean isForward() {
		return forward;
	}

	public boolean isSpace() {
		return space;
	}

	public void setRigth(boolean rigth) {
		this.rigth = rigth;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public void setForward(boolean forward) {
		this.forward = forward;
	}

	public void setSpace(boolean space) {
		this.space = space;
	}

}
