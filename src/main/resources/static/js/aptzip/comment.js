/**
 * comment CRUD utils
 */
const comment = (function(){

  let create = function(obj, callback) {
    // console.log(obj);
    // console.log('create comment~~~');

    $.ajax({
      url: obj.url,
      method: 'post',
      data: JSON.stringify(obj),
      dataType: 'json',
      contentType: 'application/json',
      success: callback,
      error: function() {
        console.error('ajax error');
      }
    });

  };

  const retrieveList = function(obj, callback) {
    // console.log('retrieve comment~~~');
    $.getJSON(obj.url, callback);
  };

  const update = function(obj, callback) {
    // console.log(obj);
    // console.log('update comment~~~');

    $.ajax({
      url: obj.url,
      method: 'put',
      data: JSON.stringify(obj),
      dataType: 'json',
      contentType: 'application/json',
      success: callback,
      error: function() {
        console.error('ajax error');
      }
    });
  };

  const remove = function(obj, callback) {
    // console.log(obj);
    // console.log('remove comment~~~');

    $.ajax({
      url: obj.url,
      method: 'delete',
      dataType: 'json',
      contentType: 'application/json',
      success: callback,
      error: function() {
        console.error('ajax error');
      }
    });
  };

  return {
    retrieveList: retrieveList,
    create: create,
    update: update,
    remove: remove
  }

})();

function deletePopup(event) {
  const POPUP = event.target.parentNode.parentNode.parentNode;
	POPUP.classList.add('d-none');
}

// todo : transform into mustache template
function renderComment(list, container, USER_ID) {
  // todo : transform into mustache template -> done
  // console.log(list);
  hbs('comments-template', list, container, USER_ID);
  editBtn();

  // let str = '';
  // let renderobj = '';

  // for (let i = 0; i < list.length; i++) {
  //   renderobj = list[i];
  //   let temporal = new Date(renderobj.updateDate);

    // str += '<div class="tt-item">'
    //       + '<div class="tt-single-topic">'
  	// 	      + '<input type="hidden" value="' + renderobj.id + '">'
    //         + '<div class="tt-item-header pt-noborder">'
    //           + '<div class="tt-item-info info-top">'
		// 		        + '<div class="tt-avatar-icon">'
		// 		          + '<i class="tt-icon"><svg>'
    //                 + '<use xlink:href="#icon-ava-' + renderobj.user.username.substring(0, 1) + '"></use>'
		// 		          + '</svg></i>'
    //             + '</div>'
		// 	          + '<div class="tt-avatar-title">'
		// 		          + '<a href="/user/info/' + renderobj.user.id + '">' + renderobj.user.username + '</a>'
		// 		        + '</div>'
  	// 			        + '<a href="#" class="tt-info-time">'
  	// 					      + '<i class="tt-icon"><svg>'
  	// 						      + '<use xlink:href="#icon-time"></use>'
  	// 							  + '</svg></i>' + (temporal.getFullYear() + '-' + (temporal.getMonth()+1) + '-' + temporal.getDate())
  	// 					    + '</a>'
  	// 				    + '</div>'
  	// 			    + '</div>'

  	// 			    + '<div class="tt-item-description">'
    //   					+ renderobj.commentContent
    //           + '</div>'

    //   				+ '<div class="tt-item-info info-bottom">'
    //   					+ '<a href="#" class="tt-icon-btn">'
    //   						+ '<i class="tt-icon"><svg>'
    //   							+ '<use xlink:href="#icon-favorite"></use>'
    //   							+ '</svg></i>'
    //   						+ '<span class="tt-text">0</span>'
    //   					+ '</a>'
    //             + '<div class="col-separator"></div>';
    // if (USER_ID == renderobj.user.id) {
    // str         += '<a href="#" class="tt-icon-btn tt-hover-02 tt-small-indent edit-comment">'
		// 							+ '<i class="tt-icon"><svg>'
		// 								+ '<use xlink:href="#icon-edit"></use>'
		// 							+ '</svg></i>'
    //             + '</a>';
    // }
    // str  			  += '<a href="#" class="tt-icon-btn tt-hover-02 tt-small-indent">'
    //   						+ '<i class="tt-icon"><svg>'
    //   								+ '<use xlink:href="#icon-share"></use>'
    //   							+ '</svg></i>'
    //   					+ '</a>'
    //   					+ '<a href="#" class="tt-icon-btn tt-hover-02 tt-small-indent">'
    //   						+ '<i class="tt-icon"><svg>'
    //   							+ '<use xlink:href="#icon-reply"></use>'
    //   						+ '</svg></i>'
    //   					+ '</a>'
    //   				+ '</div>'
    //     		+ '</div>'
    //   		+ '</div>'
    //   	 + '</div>';
    // container.innerHTML = str;

  // }
  // editBtn();

}

/*
	Comment Edit Popup switching : from bundle.js
*/
var $body = $("body"),
    $html = $("html"),
    $popupEditComment = $("#js-popup-edit-comment"),
    $btnClose = $(".tt-btn-col-close");

$(document).on("click", ".edit-comment", function(e) {
  e.preventDefault();
  // e.stopPropagation();
  if ($(this).hasClass("column-open")) {
    $btnClose.trigger("click");
    return false;
  }
  $(this).addClass("column-open");
  var ptScrollValue = $body.scrollTop() || $html.scrollTop();
  $popupEditComment.toggleClass("column-open").perfectScrollbar();
  $body
  .css("top", -ptScrollValue)
  .addClass("no-scroll")
  .append('<div class="modal-filter"></div>');

  var modalFilter = $(".modal-filter").fadeTo("fast", 1);
  if (modalFilter.length) {
    modalFilter.on("click", function() {
      $btnClose.trigger("click");
    });
  }
  editBtn();
  return false;
});

$(document).on("click", '.tt-btn-col-close', function(e) {
  e.preventDefault();
  // e.stopPropagation();
  // console.log(e.target);
  $('.edit-comment').removeClass("column-open");
  $popupEditComment.removeClass("column-open").perfectScrollbar("destroy");

  var top = parseInt($body.css("top").replace("px", ""), 10) * -1;
  $body
    .removeAttr("style")
    .removeClass("no-scroll")
    .scrollTop(top);
  $html.removeAttr("style").scrollTop(top);
  $(".modal-filter")
    .off()
    .remove();
  $("#commentHiddenId").val("");
  $("#updateCommentContent").val("");

  return false;
});