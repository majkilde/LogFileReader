function toogleDisplay( selector ){
	if( $( selector ).css("display")!="none" ){
		$( selector ).css("display", "none");
	} else {
		$( selector ).css("display", "inline");
	}
}