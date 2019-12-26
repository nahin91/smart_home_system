function getContiniouslyAllData() {
    $.ajax({
        url: '/iot/get-data',
        type: "GET",
        dataType: "xml",
        contentType: "application/xml",
        success: function (xmlData) {
            $(xmlData).find("ResponseData").each(function () {
                var totalPerson = $(this).find('totalPeople').text();
                $('#totalPeople').html(totalPerson);
            });
        }
    });
}

setInterval(getContiniouslyAllData, 5000);