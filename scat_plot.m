clc
MAX_NO_DRONES = 50;
sizedroneVecTransposed = [3 MAX_NO_DRONES];			%write transposed matrix for fscanf as it fills output array column wise
formatSpec = '%d %d %d';
    inFile = fopen('droneposition.txt','r');
    droneVecTransposed = fscanf(inFile,formatSpec,sizedroneVecTransposed);        %fscanf reads the file data and fills the output array in column order
    fclose(inFile);
    droneVec = droneVecTransposed';						%get the correct matrix
    x = droneVec(:,1);
    y = droneVec(:,2);
    z = droneVec(:,3);
    xlabel('X in mm')
    ylabel('Y in mm')
    zlabel('Z in mm')
h = scatter3(x,y,z);
xlim([-5000 5000])
ylim([-5000 5000])
zlim([0 10000])

while 1
    inFile = fopen('droneposition.txt','r');
    droneVecTransposed = fscanf(inFile,formatSpec,sizedroneVecTransposed);        %fscanf reads the file data and fills the output array in column order
    fclose(inFile);
    droneVec = droneVecTransposed';		%get the correct matrix
    try
     x = droneVec(:,1);
     y = droneVec(:,2);
     z = droneVec(:,3);
    catch
    end
    set(h,'XData',x,'YData',y,'ZData',z)
    drawnow
    pause(0.05)
end
