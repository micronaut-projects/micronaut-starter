$version = '5.0.3'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'B2AF8D26331B500A83325CC2336DB81944C54CB14870AA408FE72959B3BC7E74'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
