$version = '3.5.2'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '57696BBF4B12E26D0CE883066DFB68E519C2D131D0BBA2EFF279B578F7E5B676'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
