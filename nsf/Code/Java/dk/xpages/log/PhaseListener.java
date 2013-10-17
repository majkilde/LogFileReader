package dk.xpages.log;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;

public class PhaseListener implements javax.faces.event.PhaseListener {
	private static Logger log = LogManager.getLogger();
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void afterPhase(PhaseEvent event) {
		log.info("" + event.getPhaseId());
	}

	public void beforePhase(PhaseEvent event) {
		log.info("" + event.getPhaseId());
	}

	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}

}