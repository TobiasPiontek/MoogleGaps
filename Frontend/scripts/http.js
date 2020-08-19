$(document).ready(function() {
    $("compute").click(function() {
        $.post("http://localhost:8004/MoogleGaps", {
                name: "Donald Duck",
                city: "Duckburg"
            },
            function(data, status) {
                alert("Data: " + data + "\nStatus: " + status);
            });
    });
});