package com.subway.model;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelegateAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.utils.Pool;

public class BlinkAction extends DelegateAction {
	public static float BLINK_TIME = 0.4f;
	public static Pool<BlinkAction> pool= new Pool<BlinkAction>(4,4){
		
		@Override
		protected BlinkAction newObject() {
			return new BlinkAction(BLINK_TIME);
		}};
		
	public BlinkAction(float BLINK_TIME) {
		this.setAction(Actions.repeat(RepeatAction.FOREVER,
				Actions.sequence(new RunnableAction() {

					@Override
					public void run() {
						getActor().setColor(getActor().getColor().r,
								getActor().getColor().g,
								getActor().getColor().b, 0.1f);

					}
				}, new DelayAction(BLINK_TIME), new RunnableAction() {

					@Override
					public void run() {
						getActor().setColor(getActor().getColor().r,
								getActor().getColor().g,
								getActor().getColor().b, 1f);
					}
				}, new DelayAction(BLINK_TIME))));
	}

	@Override
	protected boolean delegate(float delta) {
		action.act(delta);
		return false;
	}

}
