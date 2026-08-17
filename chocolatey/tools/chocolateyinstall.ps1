$version = '5.1.1'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '40D1C330798E84A77E526C437CB5518D20C55E9F3FD35FA45285C70CF4435FEB'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
