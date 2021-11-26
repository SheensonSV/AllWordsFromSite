$("document").ready(function(){
    //sending uri to back
    var resp = "";
    addingDataToSite = function(data){
    console.log("we are in function addingDataToSite")
        for (var i = 0; i < data.size; i++){
        console.log("data.key - " + data.key);
        console.log("data.value - " + data.value);
        }
    }
    let responseDataFromBack;
        $('.header__inner-btn').click(function(){
            var uriOfSite = $('.header__inner-input').val();
            console.log(uriOfSite);
            $.ajax({
                method: "POST",
                url: '/push',
                contentType:"application/json",
                dataType: 'json',
//                data: uriOfSite,
                data: JSON.stringify({uriOfSiteString: uriOfSite}),
                success: function(response, textResp, xhr)
                {
                   responseDataFromBack = response;
                   console.log(xhr.status);
                   console.log(uriOfSite);
                   console.log("response in success part of function ajax");
                   resp = response;
                   console.log(response);
                   addingDataToSite(response);
                },
                error: function(error)
                {
                   console.log(uriOfSite);
                   console.log(error.status);
                },
                cache: false,
            });
            return false;
        });

});