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
                var currentSongName = $(this).find('currentSong');
                console.log(currentSongName);
                $('#currentSongName').html(currentSongName);

                $("#cardSongs").empty();
                $.each(songsList, function (index, value) {
                    if (index !== 0) {
                        var songId = "songId" + index;
                        var cardSong = '<div class="card col-md-2  col-lg-2" style="background-color: crimson; height: 210px; margin: 8px">' +
                            '<div class="card-inner" style="text-align: -moz-center" id=' + songId + '>' + value.innerHTML + '</div></div>';
                        $("#cardSongs").append(cardSong);
                        //console.log(index)
                    }
                });

                var guestList = $(this).find('guestList');
                $("#guestList").empty();
                $.each(guestList, function (index, value) {
                    if (index !== 0) {
                        var songId = "songId" + index;
                        var guestName = '<li class="list-group-item" style="border: none" id=' + songId + '>' + value.innerHTML + '</li>';
                        $("#guestList").append(guestName);
                    }
                });
            });
        }
    });
}