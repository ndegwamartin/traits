$(function(){

	var carouselWidth = 0;
	 $(".owl-carousel").owlCarousel({
			items:1,
			loop:true,
			lazyLoad:true,
			margin:carouselWidth,
			stagePadding:carouselWidth, 
			autoplay:true,
			autoplayTimeout:3500,
			autoplayHoverPause:true,
			responsiveClass:true
		});

});