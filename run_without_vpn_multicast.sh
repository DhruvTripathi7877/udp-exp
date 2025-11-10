#!/bin/bash
# This script continuously removes the VPN multicast route while your Java program runs

echo "Starting VPN multicast route suppression..."
echo "This will keep deleting utun4 multicast routes"
echo ""

# Function to delete the route
delete_vpn_route() {
    sudo route delete -net 224.0.0.0/4 -interface utun4 2>/dev/null
}

# Delete the route initially
delete_vpn_route

# Keep deleting it every 0.5 seconds in the background
(
    while true; do
        delete_vpn_route
        sleep 0.5
    done
) &
CLEANUP_PID=$!

echo "Route cleanup running in background (PID: $CLEANUP_PID)"
echo "Press Ctrl+C to stop"
echo ""

# Trap to cleanup on exit
trap "kill $CLEANUP_PID 2>/dev/null; echo 'Stopped route cleanup'; exit" INT TERM

# Wait
wait $CLEANUP_PID

