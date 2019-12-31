function getContiniouslyAllData() {
    $.ajax({
        url: '/iot/get-distance',
        type: "GET",
        dataType: "xml",
        contentType: "application/xml",
        success: function (xmlData) {
            $(xmlData).find("ResponseDistanceData").each(function () {
                var personName = $(this).find('person').text();
                $('#personName').html(personName);

                var distance = $(this).find('distance').text();
                $('#distance').html(distance);

                var songsList = $(this).find('songList');
                console.log(songsList.length);
                console.log(songsList.text());
                //$('#songID').html(songsList);
                $("#songList").empty();
                $.each(songsList, function(index, value){
                    if(index!==0){
                        var songId="songId"+index;
                        var html= '<li class="list-group-item" id='+songId+'>'+value.innerHTML+'</li>';
                        $("#songList" ).append(html);
                        console.log(index)
                    }

                });

            });
        }
    });
}