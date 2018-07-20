const postData = (url = ``, data = {}) => {
    // Default options are marked with *
    return fetch(url, {
        method: "POST", // *GET, POST, PUT, DELETE, etc.
        mode: "cors", // no-cors, cors, *same-origin
        cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
        credentials: "same-origin", // include, same-origin, *omit
        headers: {
            "Content-Type": "application/json; charset=utf-8",
            // "Content-Type": "application/x-www-form-urlencoded",
        },
        redirect: "follow", // manual, *follow, error
        referrer: "no-referrer", // no-referrer, *client
        body: JSON.stringify(data), // body data type must match "Content-Type" header
    })
        .then(response => response.text()) // parses response to JSON
        .catch(error => console.error(`Fetch Error =\n`, error));
};

const getData = (url = ``) => {
    // Default options are marked with *
    return fetch(url, {
        method: "GET", // *GET, POST, PUT, DELETE, etc.
        mode: "cors", // no-cors, cors, *same-origin
        cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
        credentials: "same-origin", // include, same-origin, *omit
        headers: {
            "Content-Type": "application/json; charset=utf-8",
            // "Content-Type": "application/x-www-form-urlencoded",
        },
        redirect: "follow", // manual, *follow, error
        referrer: "no-referrer", // no-referrer, *client
    })
        .then(response => response.json()) // parses response to JSON
        .catch(error => console.error(`Fetch Error =\n`, error));
};

document.addEventListener('DOMContentLoaded', function () {
    //POST REQUEST TO UPDATE INVOICE CONTROLLER
    //var button = document.getElementById('updateButton');
    document.getElementById('updateButton').addEventListener('click', function () {
        this.disabled = true;
        let data = {
            id: document.getElementById('selectedInvoice').value
        };

        postData('/find/update', data).then(() =>
        {
            window.location = '/success/updated';
        });
        /*getData(`/invoice/${document.getElementById('selectedInvoice').value}`).then(response=>{
            document.getElementById("editPopup").style.display = 'block';
            document.getElementById('type').innerText = response.invoiceType;
            document.getElementById('clientName').innerText = response.clien;
        })*/
        this.disabled = false;
    });
});
