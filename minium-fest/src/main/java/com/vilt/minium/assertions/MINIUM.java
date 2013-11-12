package com.vilt.minium.assertions;

import com.vilt.minium.CoreWebElements;

public class MINIUM {
	
	public static CoreWebElementsAssert assertThat(CoreWebElements<?> webElems) {
		return new CoreWebElementsAssert(webElems, CoreWebElementsAssert.class);
	}

	protected MINIUM() {
		// empty
	}
}
