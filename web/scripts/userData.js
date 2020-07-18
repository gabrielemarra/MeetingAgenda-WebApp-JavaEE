window.addEventListener("load", () => {
    makeCall("GET", "../GetUserData", null,
        function(req) {
            if (req.readyState == 4) {
                var message = req.responseText;
                if (req.status == 200) {
                    var user = JSON.parse(message);
                    document.getElementById("id_userDispName").innerText = user.displayedName;
                    document.getElementById("id_userEmail").innerText = user.email;

                }
            } else {
                //display error
            }
        }
    );
});