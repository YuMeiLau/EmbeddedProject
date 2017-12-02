

%x = rand(200,1) ; y = rand(200,1) ; z = rand(200,1) ;

%h = scatter3(x,y,z);
%xlim([-1 2])
%ylim([-1 2])
%zlim([-1 2])
%
%for i = 1:100
%    x = rand(200,1) ;
%    y = rand(200,1) ;
%    z = rand(200,1) ;
%    set(h,'XData',x,'YData',y,'ZData',z);
%    drawnow
%    pause(0.05)
%end

inFile = fopen('droneposition.txt','r');
MAX_NO_DRONES = 10;
sizedroneVec = [3 MAX_NO_DRONES];			%write transposed matrix for fscanf as it fills output array column wise
formatSpec = '%d %d %d';
xlim([-1 1])
ylim([-1 1])
zlim([-1 1])
xlabel('In m')
ylabel('In m')
zlabel('In m')

while 1
    droneVec = fscanf(inFile,formatSpec,sizedroneVec);  %fscanf reads the file data and fills the output array in column order
    droneVec = droneVec'; 				%transpose to get the proper matrix
    plot3d = scatter3(droneVec(:,0),droneVec(:,1),droneVec(:,2));
    %x = rand(200,1);
    %y = rand(200,1);
    %z = rand(200,1);
    set(plot3d,'XData',droneVec(:,0),'YData',droneVec(:,1),'ZData',droneVec(:,2));
    drawnow
    pause(0.05)
end

