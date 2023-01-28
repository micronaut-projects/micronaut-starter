$version = '3.7.6'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'BB24A8A467612587BB0DCF506ED269413896A869B67B3C474EC6395B01999489'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
