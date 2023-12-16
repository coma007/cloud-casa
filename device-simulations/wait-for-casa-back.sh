#!/bin/bash

# Waiting for the casa-back service to become available on port 8080 using curl
while true; do
  if curl -sSf http://casa-back:8080/api/device/public/simulation/getAll > /dev/null; then
    echo "casa-back is ready!"
    break
  else
    echo "Waiting for casa-back to be ready..."
    sleep 1
  fi
done

# Once casa-back is ready, start the device-simulations application
/device-simulations
