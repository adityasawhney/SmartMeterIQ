SmartMeterIQ
============

This project is developed by CU Boulder students as part of the course in Advanced Operating Systems (CSCI 5573).

Smart Meter is a great technical advancement which has tremendous potential in home automation domain. However, its adoption is hindered by legitimate privacy concerns. The goal of this project is to propose a possible approach to mitigate the privacy concerns using technology which is ubiquitous and known to work.

The main concern with Smart Meters is the granularity of the data which enables physical and behavioral analysis of the consumer in terms of the brand and make of devices installed and their house hold activities (like when do they wake up, when they are not at home).

Intuitively, we need a solution wherein consumer is in control of the data and decides what level of granularity needs to be provided to various service providers based on what services he is interested in.

So, some sort of a "local processing" unit installed within the peripheral of house would fit the bill. All the devices in the house will be connected with this device (including smart meter) and it will serve as the gateway to the outside world.

The real challenge here is that this central device has to be "tamper proof" as well as secure so that it doesn't leak the data.

We propose using Java Card platform as it is a natural fit and is designed to be tamper-proof and secure. In addition, we explore using advanced cryptography techniques such as Zero Knowledge Proof of Knowledge (using Pedersen Commitments) to enable the Utility Supplier to trust the data it is getting from the central device. 
