<template>
    <div>
        <section class="ui two column centered grid">
            <div class="column" style="width: 500px">
                <form class="ui segment larger form">
                    <div class="ui message red" v-show="error"> {{ error }}</div>
                    <div class="ui segment">
                        <div class="field">
                            <div class="ui right icon input large" :class="{ loading:spinner }">
                                <input 
                                    type="text"
                                    placeholder="Enter your address"
                                    v-model="address"
                                    id="autocomplete"
                                    />
                                    <i class="dot circle link icon" @click="locatorButtonPressed"></i>
                            </div>
                        </div>
                        <button class="ui button">Go</button>
                    </div>
                </form>
            </div>
        </section>
        <section id="map"></section>
    </div>
</template>

<script>
import axios from 'axios';
import { error } from 'naive-ui/es/_utils/naive/warn';

export default {
    data() {
        return {
            address: "",
            error: "",
            spinner: false
        }
    },
    
    mounted() {
        new google.maps.places.Autocomplete(
            document.getElementById("autocomplete"),
            {
                bonds: new google.maps.LatLngBounds(
                    new google.maps.LatLng(41.697102, 44.773674)
                )
            }
        )
    },

    methods: {
        locatorButtonPressed() {
            this.spinner = true;

            if(navigator.geolocation){
                navigator.geolocation.getCurrentPosition(
                    position => {
                        this.getAddressForm(
                            position.coords.latitude,
                            position.coords.longitude
                        )
                    },
                    error => {
                        this.error = "Location is unable to find you address. Please type your address manually.";
                        this.spinner = false;
                    }
                );
            } else {
                this.error = error.message;
                this.spinner = false;
                console.log("Your browser does not support geolocation API");
            }
        },
        getAddressForm(lat, long) {
            axios
                .get("https://maps.googleapis.com/maps/api/geocode/json?latlng=" +
                    lat + 
                    "," + 
                    long + 
                    "&key=AIzaSyAoMzQS8FynKroozr3QaLSGYKw-WglMor0"
                ).then(response => {
                    if(response.data.error_message) {
                        this.error = response.data.error_message;
                        console.log(response.data.error_message);
                    } else {
                        this.address = response.data.results[0].formatted_address;
                    }
                    this.spinner = false;
                })
                .catch(error => {
                    this.error = error.message;
                    this.spinner = false;
                    console.log(error.message);
                })
        }
    }
}
</script>


<style>
.ui.button,
.dot.circle.icon {
    background-color: red;
    color: white;
}

.pac-icon {
    display: none;
}

.pac-item {
    padding: 10px;
    font-size: 16 px;
    cursor: pointer;
}

</style>